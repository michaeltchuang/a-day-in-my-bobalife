package com.michaeltchuang.example.data.repositories

import android.util.Log
import com.algorand.algosdk.abi.Contract
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.builder.transaction.MethodCallTransactionBuilder
import com.algorand.algosdk.transaction.AtomicTransactionComposer
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.michaeltchuang.example.data.models.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.math.BigInteger
import java.util.function.Consumer

class ValidatorRepository : AlgorandRepository() {

    companion object {
        private const val TAG: String = "ValidatorRepository"
    }

    suspend fun getNumberOfValidators(
        account: Account,
        appId: Long,
        contractStr: String
    ): Long {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            try {
                val contract: Contract = Encoder.decodeFromJson(contractStr, Contract::class.java)

                val rsp = client.TransactionParams().execute()
                val tsp: TransactionParametersResponse = rsp.body()

                // create methodCallParams by builder (or create by constructor) for add method
                val mctb = MethodCallTransactionBuilder.Builder()
                mctb.applicationId(appId)
                mctb.sender(account.address.toString())
                mctb.signer(account.transactionSigner)
                mctb.suggestedParams(tsp)
                mctb.method(contract.getMethodByName("getNumValidators"))
                mctb.onComplete(Transaction.OnCompletion.NoOpOC)

                val atc = AtomicTransactionComposer()
                atc.addMethodCall(mctb.build())

                val res = atc.execute(client, 100)
                res.methodResults.forEach(
                    Consumer { methodResult: AtomicTransactionComposer.ReturnValue? ->
                        Log.d(
                            TAG,
                            methodResult.toString(),
                        )
                    },
                )
                result = res
            } catch (e: Exception) {
                Log.e(TAG, "" + e.toString())
            }

        }
        val validatorCount = if (result?.confirmedRound == null) {
            0L
        } else if (result?.methodResults == null) {
            0L
        } else {
            // successful result
            result?.methodResults?.get(0)?.value as BigInteger
        }
        return validatorCount.toLong()
    }
















    suspend fun getValidatorsList(): Flow<NetworkResult<List<Validator>>> {
        return toResultFlow {
            val validatorList = mutableListOf<Validator>()
            withContext(Dispatchers.IO) {

                var validator = Validator(
                    id = 1,
                    name = "reti.algo",
                    algoStaked = 25000,
                    apr = 3.03,
                    percentToValidator = 5,
                    epochRoundLength = 1296,
                    minEntryStake = 5
                )
                validatorList.add(validator)

                validator = Validator(
                    id = 2,
                    name = "steventest.algo",
                    algoStaked = 25000,
                    apr = 3.03,
                    percentToValidator = 5,
                    epochRoundLength = 1296,
                    minEntryStake = 5
                )
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)
                validatorList.add(validator)

            }

            NetworkResult.Success(validatorList)
        }
    }
}