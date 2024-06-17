package com.michaeltchuang.example.data.repositories

import android.util.Log
import com.algorand.algosdk.abi.Contract
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.builder.transaction.MethodCallTransactionBuilder
import com.algorand.algosdk.transaction.AtomicTransactionComposer
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.util.function.Consumer

class CoinFlipperRepository : AlgorandRepository() {
    companion object {
        private const val TAG: String = "CoinFlipperRepository"
    }

    suspend fun appFlipCoin(
        account: Account,
        appId: Long,
        contractStr: String,
        amount: Int,
        isHeads: Boolean,
    ): AtomicTransactionComposer.ExecuteResult? {
        return withContext(Dispatchers.IO) {
            try {
                val contract: Contract = Encoder.decodeFromJson(contractStr, Contract::class.java)

                val rsp = client.TransactionParams().execute()
                val tsp: TransactionParametersResponse = rsp.body()
                val tws = createTransactionWithSigner(account, appId, amount)
                val methodArgs = listOf(tws, isHeads)

                // create methodCallParams by builder (or create by constructor) for add method
                val mctb = MethodCallTransactionBuilder.Builder()
                mctb.applicationId(appId)
                mctb.sender(account.address.toString())
                mctb.signer(account.transactionSigner)
                mctb.suggestedParams(tsp)
                mctb.method(contract.getMethodByName("flip_coin"))
                mctb.methodArguments(methodArgs)
                mctb.onComplete(Transaction.OnCompletion.NoOpOC)

                val atc = AtomicTransactionComposer()
                // atc.addTransaction(tws);
                atc.addMethodCall(mctb.build())
                val res = atc.execute(client, 100)

                res.methodResults.forEach(
                    Consumer<AtomicTransactionComposer.ReturnValue> { methodResult: AtomicTransactionComposer.ReturnValue? ->
                        Log.d(
                            TAG,
                            methodResult.toString(),
                        )
                    },
                )

                Log.d(TAG, "flip coin call success for app-id: $appId")
                res
            } catch (e: Exception) {
                Log.e(TAG, "" + e.toString())
                null
            }
        }
    }

    suspend fun appSettleBet(
        account: Account,
        appId: Long,
        contractStr: String,
        randomBeaconApp: BigInteger,
    ): AtomicTransactionComposer.ExecuteResult? {
        return withContext(Dispatchers.IO) {
            try {
                val contract: Contract = Encoder.decodeFromJson(contractStr, Contract::class.java)

                val rsp = client.TransactionParams().execute()
                val tsp: TransactionParametersResponse = rsp.body()
                val methodArgs = listOf(account.address, randomBeaconApp)

                // create methodCallParams by builder (or create by constructor) for add method
                val mctb = MethodCallTransactionBuilder.Builder()
                mctb.applicationId(appId)
                mctb.sender(account.address.toString())
                mctb.signer(account.transactionSigner)
                mctb.suggestedParams(tsp)
                mctb.method(contract.getMethodByName("settle"))
                mctb.methodArguments(methodArgs)
                mctb.onComplete(Transaction.OnCompletion.NoOpOC)

                val atc = AtomicTransactionComposer()
                // atc.addTransaction(tws);
                atc.addMethodCall(mctb.build())

                val res = atc.execute(client, 100)
                res.methodResults.forEach(
                    Consumer<AtomicTransactionComposer.ReturnValue> { methodResult: AtomicTransactionComposer.ReturnValue? ->
                        Log.d(
                            TAG,
                            methodResult.toString(),
                        )
                    },
                )
                Log.d(TAG, "flip coin call success for app-id: $appId")
                res
            } catch (e: Exception) {
                Log.e(TAG, "" + e.toString())
                null
            }
        }
    }
}
