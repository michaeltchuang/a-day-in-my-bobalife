package com.michaeltchuang.example.data.repositories

import android.content.Context
import android.util.Log
import com.algorand.algosdk.abi.Contract
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.builder.transaction.MethodCallTransactionBuilder
import com.algorand.algosdk.transaction.AtomicTransactionComposer
import com.algorand.algosdk.transaction.Transaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.michaeltchuang.example.data.local.AppSettings
import com.michaeltchuang.example.data.local.ExampleDatabase
import com.michaeltchuang.example.data.local.dao.ExampleDao
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import com.michaeltchuang.example.data.models.Validator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.math.BigInteger
import java.util.function.Consumer

class ValidatorRepository(
    context: Context,
) : AlgorandRepository(), KoinComponent {
    companion object {
        private const val TAG: String = "ValidatorRepository"
    }

    private var database: ExampleDao
    private val appSettings: AppSettings by inject()

    val coroutineScope = CoroutineScope(Dispatchers.Default)

    private suspend fun loadData() {
        try {
            // load data to db
        } catch (e: Exception) {
            // TODO surface this to UI/option to retry etc ?
            println("Exception reading data: $e")
        }
    }

    init {
        database = ExampleDatabase.getDatabase(context).getDao()
    }

    fun insertValidatorIntoDb() =
        coroutineScope.launch {
            var validator =
                ValidatorEntity(
                    id = 1,
                    name = "reti.algo",
                    algoStaked = 25000,
                    apr = 3.03,
                    percentToValidator = 5,
                    epochRoundLength = 1296,
                    minEntryStake = 5,
                )
            database.insertValidator(validator)

            validator =
                ValidatorEntity(
                    id = 2,
                    name = "steventest.algo",
                    algoStaked = 25000,
                    apr = 3.03,
                    percentToValidator = 5,
                    epochRoundLength = 1296,
                    minEntryStake = 5,
                )
            database.insertValidator(validator)
        }

    // get data from db
    fun getAllValidatorsFromDb(): Flow<List<ValidatorEntity>> {
        return database.getAllValidatorsAsFlow()
    }

    suspend fun getNumberOfValidators(
        account: Account,
        appId: Long,
        contractStr: String,
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
        val validatorCount =
            if (result?.confirmedRound == null) {
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
                var validator =
                    Validator(
                        id = 1,
                        name = "reti.algo",
                        algoStaked = 25000,
                        apr = 3.03,
                        percentToValidator = 5,
                        epochRoundLength = 1296,
                        minEntryStake = 5,
                    )
                validatorList.add(validator)

                validator =
                    Validator(
                        id = 2,
                        name = "steventest.algo",
                        algoStaked = 25000,
                        apr = 3.03,
                        percentToValidator = 5,
                        epochRoundLength = 1296,
                        minEntryStake = 5,
                    )
                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
//                validatorList.add(validator)
            }

            NetworkResult.Success(validatorList)
        }
    }
}
