package com.michaeltchuang.example.data.repositories

import android.content.Context
import com.algorand.algosdk.abi.Contract
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.AppBoxReference
import com.algorand.algosdk.transaction.AtomicTransactionComposer
import com.algorand.algosdk.util.Encoder
import com.michaeltchuang.example.data.local.ExampleDatabase
import com.michaeltchuang.example.data.local.dao.ExampleDao
import com.michaeltchuang.example.data.local.entities.ValidatorEntity
import com.michaeltchuang.example.data.models.Valid
import com.michaeltchuang.example.data.models.ValidatorConfig
import com.michaeltchuang.example.data.network.NfdApi
import com.michaeltchuang.example.utils.Constants
import com.michaeltchuang.example.utils.getValidatorListBoxName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.math.BigInteger


class ValidatorRepository(
    context: Context,
) : AlgorandRepository(),
    KoinComponent {
    companion object {
        private const val TAG: String = "ValidatorRepository"
    }

    private var database: ExampleDao
    private val nfdApi: NfdApi by inject()

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
        //loadValidatorDataIntoDb()
    }

    fun loadValidatorDataIntoDb() {
        val validatorList: List<ValidatorEntity> =
            listOf(
                ValidatorEntity(
                    id = 1,
                    name = "reti.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 5,
                    epochRoundLength = 1296,
                    minEntryStake = 5,
                ),
                ValidatorEntity(
                    id = 2,
                    name = "steventest.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 1,
                    epochRoundLength = 32000,
                    minEntryStake = 1000,
                ),
                ValidatorEntity(
                    id = 3,
                    name = "algoleaguestestnet.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 5,
                    epochRoundLength = 10000,
                    minEntryStake = 10,
                ),
                ValidatorEntity(
                    id = 7,
                    name = "DTM6B4ZQ5HC7SMDG3INL672WWWPRIXZUSABXHMIMDXGUXU63PPD7S7KLBA",
                    algoStaked = null,
                    apy = null,
                    avatar = null,
                    percentToValidator = 1,
                    epochRoundLength = 31104,
                    minEntryStake = 1,
                ),
                ValidatorEntity(
                    id = 8,
                    name = "reti.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 5,
                    epochRoundLength = 1296,
                    minEntryStake = 5,
                ),
                ValidatorEntity(
                    id = 9,
                    name = "steventest.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 1,
                    epochRoundLength = 32000,
                    minEntryStake = 1000,
                ),
                ValidatorEntity(
                    id = 10,
                    name = "algoleaguestestnet.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 5,
                    epochRoundLength = 10000,
                    minEntryStake = 10,
                ),
                ValidatorEntity(
                    id = 11,
                    name = "DTM6B4ZQ5HC7SMDG3INL672WWWPRIXZUSABXHMIMDXGUXU63PPD7S7KLBA",
                    algoStaked = null,
                    apy = null,
                    percentToValidator = 1,
                    epochRoundLength = 31104,
                    minEntryStake = 1,
                ),
                ValidatorEntity(
                    id = 12,
                    name = "reti.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 5,
                    epochRoundLength = 1296,
                    minEntryStake = 5,
                ),
                ValidatorEntity(
                    id = 13,
                    name = "steventest.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 1,
                    epochRoundLength = 32000,
                    minEntryStake = 1000,
                ),
                ValidatorEntity(
                    id = 14,
                    name = "algoleaguestestnet.algo",
                    algoStaked = 25000,
                    apy = 3.03,
                    avatar = null,
                    percentToValidator = 5,
                    epochRoundLength = 10000,
                    minEntryStake = 10,
                ),
                ValidatorEntity(
                    id = 15,
                    name = "DTM6B4ZQ5HC7SMDG3INL672WWWPRIXZUSABXHMIMDXGUXU63PPD7S7KLBA",
                    algoStaked = null,
                    apy = null,
                    avatar = null,
                    percentToValidator = 1,
                    epochRoundLength = 31104,
                    minEntryStake = 1,
                ),
            )

        coroutineScope.launch {
            validatorList.forEach {
                insertValidatorIntoDb(it)
            }
        }
    }

    fun insertValidatorIntoDb(validatorEntity: ValidatorEntity) =
        coroutineScope.launch {
            database.insertValidator(validatorEntity)
        }

    // get data from db
    fun getAllValidatorsFromDb(): Flow<List<ValidatorEntity>> = database.getAllValidatorsAsFlow()

    fun getValidatorByIdFromDb(validatorId: Int): Flow<ValidatorEntity?> = database.getValidatorByIdAsFlow(validatorId)

    suspend fun getNumberOfValidators(
        account: Account,
        contractStr: String,
    ): Long {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            val methodArgs = null
            val boxReferences = null
            val contract: Contract = Encoder.decodeFromJson(contractStr, Contract::class.java)
            val method = contract.getMethodByName("getNumValidators")

            result = methodCallTransaction(
                appId = Constants.RETI_APP_ID_TESTNET,
                account = account,
                method = method,
                methodArgs = methodArgs,
                boxReferences = boxReferences
            )
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



    suspend fun fetchValidatorInfo(
        account: Account,
        contractStr: String,
        validatorId: BigInteger,
    ) {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            val methodArgs = listOf(validatorId)
            val boxReferences = listOf(
                AppBoxReference(0, getValidatorListBoxName(validatorId.toLong())),
                AppBoxReference(0, getValidatorListBoxName(0.toLong()))
            )
            val contract: Contract = Encoder.decodeFromJson(contractStr, Contract::class.java)
            val method = contract.getMethodByName("getValidatorConfig")

            result = methodCallTransaction(
                appId = Constants.RETI_APP_ID_TESTNET,
                account = account,
                method = method,
                methodArgs = methodArgs,
                boxReferences = boxReferences
            )
        }

        if (result == null || result?.confirmedRound == null || result?.methodResults == null) {
            return
        } else {
            val validatorConfig = validatorConfigFromABIReturn(result?.methodResults?.get(0)?.value as Any)

            validatorConfig?.let{
                try {
                    var valid: Valid? = null
                    val nfdRecord = if (validatorConfig.NFDForInfo.toInt() != 0) {
                        valid = nfdApi.isValid(validatorConfig.NFDForInfo.toInt())
                        nfdApi.fetchNfd(valid.name)
                    } else {
                        null
                    }

                    val avatar = nfdRecord?.properties?.userDefined?.avatar ?:
                    (if (valid?.name?.contains(".algo") == true) { "https://app.nf.domains/img/nfd-image-placeholder.jpg" } else null)

                    val validatorEntity = ValidatorEntity(
                        id = validatorId.toInt(),
                        name = valid?.name ?: validatorConfig.Owner.toString(),
                        algoStaked = 25000,
                        apy = 3.03,
                        avatar = avatar,
                        percentToValidator = validatorConfig.PercentToValidator.toInt(),
                        epochRoundLength = validatorConfig.EpochRoundLength.toInt(),
                        minEntryStake = validatorConfig.MinEntryStake.toInt(),
                        inactive = false
                    )
                    insertValidatorIntoDb(validatorEntity)
                } catch (e: Exception) {
                    //ignore if network error
                }
            }

        }
    }

    suspend fun addStakeToValidator(
        account: Account,
        contractStr: String,
        validatorId: Long,
        amount: Int,
    ): List<ValidatorEntity> {
        var result: AtomicTransactionComposer.ExecuteResult? = null
        withContext(Dispatchers.IO) {
            val tws = createTransactionWithSigner(account, Constants.RETI_APP_ID_TESTNET, amount)
            val methodArgs = listOf(tws, validatorId, 0L)
            val boxReferences = listOf(
                AppBoxReference(0, getValidatorListBoxName(validatorId.toLong())),
                AppBoxReference(0, getValidatorListBoxName(0.toLong()))
            )
            val contract: Contract = Encoder.decodeFromJson(contractStr, Contract::class.java)
            val method = contract.getMethodByName("addStake")

            result = methodCallTransaction(
                appId = Constants.RETI_APP_ID_TESTNET,
                account = account,
                method = method,
                methodArgs = methodArgs,
                boxReferences = boxReferences
            )
        }
        val validatorCount =
            if (result?.confirmedRound == null) {
                0L
            } else if (result?.methodResults == null) {
                0L
            } else {
                // successful result
                result?.methodResults?.get(0)?.value as BigInteger //poolId: number
                result?.methodResults?.get(1)?.value as BigInteger //poolAppId: number
                result?.methodResults?.get(2)?.value as BigInteger //validatorId: number
            }

        val output: List<ValidatorEntity> = mutableListOf<ValidatorEntity>()
        return output
    }

}


fun validatorConfigFromABIReturn(returnVal: Any): ValidatorConfig? {
    return try {
        val arrReturn = returnVal as? Array<*>
            ?: throw IllegalArgumentException("unknown value returned from abi, type:${returnVal.javaClass.simpleName}")

        if (arrReturn.size != 18) {
            throw IllegalArgumentException("should be 18 elements returned in ValidatorConfig response")
        }

        val config = ValidatorConfig()
        config.ID = (arrReturn[0] as BigInteger)
        config.Owner = (arrReturn[1] as Address)
        config.Manager = (arrReturn[2] as Address)
        config.NFDForInfo = (arrReturn[3] as BigInteger)
        config.EntryGatingType = (arrReturn[4] as BigInteger)
        config.EntryGatingAddress = (arrReturn[5] as Address)
        config.EntryGatingAssets = (arrReturn[6] as Array<*>)
        config.GatingAssetMinBalance = (arrReturn[7] as BigInteger)
        config.RewardTokenId = (arrReturn[8] as BigInteger)
        config.RewardPerPayout = (arrReturn[9] as BigInteger)
        config.EpochRoundLength = (arrReturn[10] as BigInteger)
        config.PercentToValidator = (arrReturn[11] as BigInteger)
        config.ValidatorCommissionAddress = (arrReturn[12] as Address)
        config.MinEntryStake = (arrReturn[13] as BigInteger)
        config.MaxAlgoPerPool = (arrReturn[14] as BigInteger)
        config.PoolsPerNode = (arrReturn[15] as BigInteger)
        config.SunsettingOn = (arrReturn[16] as BigInteger)
        config.SunsettingTo = (arrReturn[17] as BigInteger)

        config
    } catch (e: Exception) {
        null
    }
}

//fun getNfdAvatarUrl(nfd: Nfd): String {
//    val baseUrl = getNfdAppFromViteEnvironment()
//    val url = nfd.properties?.userDefined?.avatar ?: nfd.properties?.verified?.avatar
//
//    val isAvailable = nfd.state == "available"
//    val isForSale = nfd.state == "forSale"
//    val isReserved = nfd.state == "reserved"
//    val isCurated = nfd.category == "curated"
//
//    if (url.isNullOrBlank() && isCurated) {
//        return "$baseUrl/img/nfd-image-placeholder_gold.jpg"
//    }
//
//    val showAvailablePlaceholder = isAvailable || isForSale || isReserved
//
//    if (url.isNullOrBlank() && showAvailablePlaceholder) {
//        return "$baseUrl/img/nfd-image-placeholder_gray.jpg"
//    }
//
//    if (url.isNullOrBlank()) {
//        return "$baseUrl/img/nfd-image-placeholder.jpg"
//    }
//
//    return url
//}




