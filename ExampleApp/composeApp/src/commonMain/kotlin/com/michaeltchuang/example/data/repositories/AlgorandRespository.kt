package com.michaeltchuang.example.data.repositories

import android.util.Log
import com.algorand.algosdk.abi.Method
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.builder.transaction.MethodCallTransactionBuilder
import com.algorand.algosdk.builder.transaction.PaymentTransactionBuilder
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.AppBoxReference
import com.algorand.algosdk.transaction.AtomicTransactionComposer
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.transaction.TransactionWithSigner
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.Utils
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.common.Response
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.michaeltchuang.example.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.algorand.algosdk.v2.client.model.Account as AccountInfo

open class AlgorandRepository {
    companion object {
        private const val TAG: String = "AlgorandRepository"
    }

    var client: AlgodClient =
        AlgodClient(
            Constants.ALGOD_API_ADDR,
            Constants.ALGOD_PORT,
            Constants.ALGOD_API_TOKEN,
            Constants.ALGOD_API_TOKEN_KEY,
        )

    val txHeaders = arrayOf("Content-Type")
    val txValues = arrayOf("application/x-binary")

    fun generateAlgodPair(): Account = Account()

    fun recoverAccount(passPhrase: String): Account? {
        try {
            return Account(passPhrase)
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getAccountInfo(account: Account): AccountInfo? =
        withContext(Dispatchers.IO) {
            try {
                val respAcct = client.AccountInformation(account.getAddress()).execute()
                val accountInfo = respAcct.body()
                Log.d(TAG, String.format("Account Balance: %d microAlgos", accountInfo.amount))
                accountInfo
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                null
            }
        }

    suspend fun appOptIn(
        account: Account,
        appId: Long,
    ): PendingTransactionResponse? =
        withContext(Dispatchers.IO) {
            try {
                // define sender as creator
                val sender: Address = account.address
                val params: TransactionParametersResponse =
                    client.TransactionParams().execute().body()

                // create unsigned transaction
                val txn: Transaction =
                    Transaction
                        .ApplicationOptInTransactionBuilder()
                        .sender(sender)
                        .suggestedParams(params)
                        .applicationId(appId)
                        .build()

                // sign transaction
                val signedTxn: SignedTransaction = account.signTransaction(txn)

                // send to network
                val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(signedTxn)
                val txnId: String =
                    client
                        .RawTransaction()
                        .rawtxn(encodedTxBytes)
                        .execute(txHeaders, txValues)
                        .body()
                        .txId
                Log.d(TAG, "Transaction $txnId")

                // Wait for transaction confirmation
                val pTrx: PendingTransactionResponse = Utils.waitForConfirmation(client, txnId, 10)
                Log.d(TAG, "Closed out from app-id: $appId and $pTrx")
                pTrx
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                null
            }
        }

    suspend fun closeOutApp(
        account: Account,
        appId: Long,
    ): PendingTransactionResponse? =
        withContext(Dispatchers.IO) {
            try {
                // define sender as creator
                val sender: Address = account.address
                val params: TransactionParametersResponse =
                    client.TransactionParams().execute().body()

                // create unsigned transaction
                val txn: Transaction =
                    Transaction
                        .ApplicationCloseTransactionBuilder()
                        .sender(sender)
                        .suggestedParams(params)
                        .applicationId(appId)
                        .build()

                // sign transaction
                val signedTxn: SignedTransaction = account.signTransaction(txn)

                // send to network
                val encodedTxBytes: ByteArray = Encoder.encodeToMsgPack(signedTxn)
                val txnId: String =
                    client
                        .RawTransaction()
                        .rawtxn(encodedTxBytes)
                        .execute(txHeaders, txValues)
                        .body()
                        .txId
                Log.d(TAG, "Transaction $txnId")

                // Wait for transaction confirmation
                val pTrx: PendingTransactionResponse = Utils.waitForConfirmation(client, txnId, 10)
                Log.d(TAG, "Closed out from app-id: $appId and $pTrx")
                pTrx
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                null
            }
        }

    suspend fun getCurrentRound(): Long =
        withContext(Dispatchers.IO) {
            try {
                val params: TransactionParametersResponse =
                    client.TransactionParams().execute().body()
                        ?: client.TransactionParams().execute().body() ?: client
                        .TransactionParams()
                        .execute()
                        .body()
                params.lastRound
            } catch (e: Exception) {
                0L
            }
        }

    suspend fun createTransactionWithSigner(
        account: Account,
        appId: Long,
        amount: Int,
    ): TransactionWithSigner? =
        withContext(Dispatchers.IO) {
            try {
                // Get suggested params from client
                val rsp: Response<TransactionParametersResponse> =
                    client.TransactionParams().execute()
                val sp: TransactionParametersResponse = rsp.body() ?: rsp.body() ?: rsp.body()

                // Create a transaction
                val ptxn =
                    PaymentTransactionBuilder
                        .Builder()
                        .suggestedParams(sp)
                        .amount(amount)
                        .sender(account.address)
                        .receiver(Address.forApplication(appId))
                        .build()

                // Construct TransactionWithSigner
                val tws = TransactionWithSigner(ptxn, account.transactionSigner)
                tws
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
                null
            }
        }

    fun methodCallTransaction(
        appId: Long,
        account: Account,
        method: Method,
        methodArgs: List<Any?>? = null,
        boxReferences: List<AppBoxReference>? = null
    ) : AtomicTransactionComposer.ExecuteResult? {
        try {
            val rsp = client.TransactionParams().execute()
            val tsp: TransactionParametersResponse = rsp.body()

            val mctb = MethodCallTransactionBuilder.Builder()
            mctb.applicationId(appId)
            mctb.sender(account.address.toString())
            mctb.signer(account.transactionSigner)
            mctb.method(method)
            methodArgs?.let { mctb.methodArguments(methodArgs) }
            boxReferences?.let { mctb.boxReferences(boxReferences) }
            mctb.onComplete(Transaction.OnCompletion.NoOpOC)
            mctb.suggestedParams(tsp)

            val atc = AtomicTransactionComposer()
            atc.addMethodCall(mctb.build())

            return atc.execute(client, 250)
        } catch (e: Exception) {
            Log.e(TAG, "" + e.toString())
        }
        return null
    }
}
