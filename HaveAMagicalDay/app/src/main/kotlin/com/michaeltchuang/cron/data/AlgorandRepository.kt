package com.michaeltchuang.cron.data

import com.michaeltchuang.cron.utils.Constants
import org.json.JSONObject
import uniffi.algokit_composer_ffi.CommonTxnParams
import uniffi.algokit_composer_ffi.ComposerParams
import uniffi.algokit_composer_ffi.PaymentParams
import uniffi.algokit_composer_ffi.SuggestedParams
import uniffi.algokit_composer_ffi.TxnParams
import uniffi.algokit_composer_ffi.TxnParamsKind
import uniffi.algokit_composer_ffi.compose
import uniffi.algokit_composer_ffi.signTransactions
import uniffi.algokit_crypto_ffi.ed25519PublicKeyFromSeed
import uniffi.algokit_crypto_ffi.seedFromMnemonic
import uniffi.algokit_transact_ffi.addressFromPublicKey
import java.net.HttpURLConnection
import java.net.URI
import java.util.Base64

private const val DEFAULT_VALIDITY_WINDOW_ROUNDS = 1000L
private const val DEFAULT_MIN_FEE = 1000L
private const val HTTP_TIMEOUT_MS = 15_000
private const val CONFIRMATION_POLL_DELAY_MS = 1000L
private const val MAX_CONFIRMATION_ROUNDS = 10

/**
 * An Algorand account recovered from its 25-word mnemonic, backed by the AlgoKit Rust core
 * (algokit_crypto_ffi / algokit_transact_ffi) instead of the Java algosdk's `Account`.
 */
data class AlgorandAccount(
    val address: String,
    // 32-byte Ed25519 seed, used directly by algokit_composer_ffi's signTransactions.
    val secretKey: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AlgorandAccount

        if (address != other.address) return false
        if (!secretKey.contentEquals(other.secretKey)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + secretKey.contentHashCode()
        return result
    }
}

class AlgorandRepository {
    private val algodBaseUrl = "${Constants.ALGOD_API_ADDR}:${Constants.ALGOD_PORT}"

    fun recoverAccount(passPhrase: String): AlgorandAccount? =
        try {
            val seed = seedFromMnemonic(passPhrase)
            val publicKey = ed25519PublicKeyFromSeed(seed)
            val address = addressFromPublicKey(publicKey)
            AlgorandAccount(address = address, secretKey = seed)
        } catch (e: Exception) {
            println(e.toString())
            null
        }

    fun sendPayment(
        account: AlgorandAccount,
        receiverAddress: String,
        amount: Int,
        note: String,
    ): String? =
        try {
            val suggestedParams = fetchSuggestedParams()

            val txnParams =
                TxnParams(
                    kind = TxnParamsKind.PAYMENT,
                    payment =
                        PaymentParams(
                            common =
                                CommonTxnParams(
                                    sender = account.address,
                                    note = note.toByteArray(Charsets.UTF_8),
                                ),
                            receiver = receiverAddress,
                            amount = amount.toULong(),
                        ),
                )

            // Build the unsigned (canonical) transaction bytes.
            val unsignedTxns =
                compose(
                    txnParams = listOf(txnParams),
                    composerParams = ComposerParams(suggestedParams = suggestedParams),
                )

            // Pair the transaction bytes with the 32-byte Ed25519 secret key to produce a
            // ready-to-broadcast signed transaction.
            val signedTxn = signTransactions(unsignedTxns, listOf(account.secretKey)).single()

            val txnId = submitTransaction(signedTxn)
            waitForConfirmation(txnId)

            println("${account.address} sent $amount microAlgos to $receiverAddress for transaction $txnId")
            txnId
        } catch (e: Exception) {
            println(e.toString())
            null
        }

    private fun fetchSuggestedParams(): SuggestedParams {
        val json = httpGetJson("/v2/transactions/params") ?: throw Exception("Params retrieval error")
        val lastRound = json.getLong("last-round")
        val genesisHash = Base64.getDecoder().decode(json.getString("genesis-hash"))
        val genesisId = json.optString("genesis-id", "")
        val suggestedFee = json.optLong("fee", 0L)
        val minFee = json.optLong("min-fee", DEFAULT_MIN_FEE)
        return SuggestedParams(
            fee = (if (suggestedFee > 0L) suggestedFee else minFee).toULong(),
            flatFee = suggestedFee <= 0L,
            firstRoundValid = lastRound.toULong(),
            lastRoundValid = (lastRound + DEFAULT_VALIDITY_WINDOW_ROUNDS).toULong(),
            genesisHash = genesisHash,
            genesisId = genesisId,
        )
    }

    private fun submitTransaction(signedTxnBytes: ByteArray): String {
        val connection = openConnection("/v2/transactions", "POST")
        connection.setRequestProperty("Content-Type", "application/x-binary")
        connection.doOutput = true
        connection.outputStream.use { it.write(signedTxnBytes) }

        val code = connection.responseCode
        val body = readBody(connection, code)
        if (code !in 200..299) {
            throw Exception("Failed to send transaction: HTTP $code $body")
        }
        return JSONObject(body).getString("txId")
    }

    private fun waitForConfirmation(txnId: String) {
        repeat(MAX_CONFIRMATION_ROUNDS) {
            val json = httpGetJson("/v2/transactions/pending/$txnId")
            val confirmedRound = json?.optLong("confirmed-round", 0L) ?: 0L
            if (confirmedRound > 0L) return
            Thread.sleep(CONFIRMATION_POLL_DELAY_MS)
        }
        throw Exception("Transaction $txnId was not confirmed after $MAX_CONFIRMATION_ROUNDS rounds")
    }

    private fun httpGetJson(path: String): JSONObject? {
        val connection = openConnection(path, "GET")
        val code = connection.responseCode
        val body = readBody(connection, code)
        if (code !in 200..299) return null
        return body.takeIf { it.isNotBlank() }?.let { JSONObject(it) }
    }

    private fun openConnection(
        path: String,
        method: String,
    ): HttpURLConnection {
        val connection = URI(algodBaseUrl + path).toURL().openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.connectTimeout = HTTP_TIMEOUT_MS
        connection.readTimeout = HTTP_TIMEOUT_MS
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty(Constants.ALGOD_API_TOKEN_KEY, Constants.ALGOD_API_TOKEN)
        return connection
    }

    private fun readBody(
        connection: HttpURLConnection,
        code: Int,
    ): String =
        (if (code in 200..299) connection.inputStream else connection.errorStream)
            ?.bufferedReader()
            ?.use { it.readText() }
            .orEmpty()
}
