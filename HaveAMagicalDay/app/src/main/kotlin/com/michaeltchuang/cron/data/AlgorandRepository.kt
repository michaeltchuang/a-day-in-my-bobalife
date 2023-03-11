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
import com.michaeltchuang.cron.txHeaders
import com.michaeltchuang.cron.txValues

import com.michaeltchuang.cron.utils.Constants


class AlgorandRepository() {
    private val TAG: String = "AlgorandRepository"
    private var client: AlgodClient = AlgodClient(
        Constants.ALGOD_API_ADDR,
        Constants.ALGOD_PORT,
        Constants.ALGOD_API_TOKEN,
        Constants.ALGOD_API_TOKEN_KEY
    )

    fun recoverAccount(passPhrase : String) : Account? {
        try {
            return Account(passPhrase)
        } catch (e: Exception) {
            println(e.toString())
            return null
        }
    }

    fun sendPayment(account: Account, appId: Long, amount: Int) : PendingTransactionResponse? {
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
            val sp: TransactionParametersResponse = resp.body()
                ?: throw java.lang.Exception("Params retrieval error")
            val note = "Have a magical day everyone"

            // Create a transaction
            val ptxn = PaymentTransactionBuilder.Builder()
                .suggestedParams(sp)
                .amount(amount)
                .sender(account.address)
                .receiver(Address.forApplication(appId))
                .noteUTF8(note)
                .build()

            // sign transaction
            val signedTxn: SignedTransaction = account.signTransaction(ptxn)

            // send to network
            val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(signedTxn)
            val txnId = client.RawTransaction().rawtxn(encodedTxBytes)
                .execute(txHeaders, txValues).body().txId

            // Wait for transaction confirmation
            val pTrx: PendingTransactionResponse = Utils.waitForConfirmation(client, txnId, 10)
            println("${account.address} sent ${amount} microAlgos to ${Address.forApplication(appId)} for transaction ${txnId}")
            return pTrx
        } catch(e: Exception) {
            println(e.toString())
            return null
        }
    }
}
