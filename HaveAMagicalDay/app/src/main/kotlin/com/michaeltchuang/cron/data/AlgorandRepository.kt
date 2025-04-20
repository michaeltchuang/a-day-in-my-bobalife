package com.michaeltchuang.cron.data

import com.algorand.algosdk.account.Account
import com.algorand.algosdk.builder.transaction.PaymentTransactionBuilder
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.Utils
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.michaeltchuang.cron.utils.Constants

class AlgorandRepository() {
    private var client: AlgodClient =
        AlgodClient(
            Constants.ALGOD_API_ADDR,
            Constants.ALGOD_PORT,
            Constants.ALGOD_API_TOKEN,
            Constants.ALGOD_API_TOKEN_KEY,
        )

    val txHeaders = arrayOf("Content-Type")
    val txValues = arrayOf("application/x-binary")

    fun recoverAccount(passPhrase: String): Account? {
        try {
            return Account(passPhrase)
        } catch (e: Exception) {
            println(e.toString())
            return null
        }
    }

    fun sendPayment(
        account: Account,
        receiverAddress: String,
        amount: Int,
        note: String,
    ): String? {
        try {
            val respAcct = client.AccountInformation(account.getAddress()).execute()
            if (!respAcct.isSuccessful) {
                throw java.lang.Exception(respAcct.message())
            }

            // Get suggested params from client
            val resp = client.TransactionParams().execute()
            if (!resp.isSuccessful()) {
                throw java.lang.Exception(resp.message())
            }
            val sp: TransactionParametersResponse =
                resp.body()
                    ?: throw java.lang.Exception("Params retrieval error")

            // Convert string address to Address object
            val receiverAddr = Address(receiverAddress)

            // Create a transaction
            val ptxn =
                PaymentTransactionBuilder.Builder()
                    .suggestedParams(sp)
                    .amount(amount)
                    .sender(account.address)
                    .receiver(receiverAddr)
                    .noteUTF8(note)
                    .build()

            // sign transaction
            val signedTxn: SignedTransaction = account.signTransaction(ptxn)

            // send to network
            val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(signedTxn)
            val txResponse =
                client.RawTransaction().rawtxn(encodedTxBytes)
                    .execute(txHeaders, txValues)

            // Check if the response is successful and has a body
            if (!txResponse.isSuccessful) {
                throw Exception("Failed to send transaction: ${txResponse.message()}")
            }

            // Get the transaction ID
            val txnId =
                txResponse.body()?.txId
                    ?: signedTxn.transactionID // Fallback to getting ID from signed transaction

            if (txnId == null) {
                throw Exception("Failed to get transaction ID")
            }

            // Wait for transaction confirmation
            val pTrx: PendingTransactionResponse = Utils.waitForConfirmation(client, txnId, 10)
            println("${account.address} sent $amount microAlgos to $receiverAddress for transaction $txnId")
            return txnId
        } catch (e: Exception) {
            println(e.toString())
            return null
        }
    }
}
