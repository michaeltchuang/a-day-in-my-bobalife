package com.michaeltchuang.example.data.repositories

import android.content.Context
import com.algorand.algosdk.abi.Contract
import com.algorand.algosdk.account.Account
import com.algorand.algosdk.transaction.AtomicTransactionComposer
import com.algorand.algosdk.util.Encoder
import com.michaeltchuang.example.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import java.math.BigInteger

class CoinFlipperRepository(context: Context) : AlgorandRepository(),
    KoinComponent
{
    companion object {
        private const val TAG: String = "CoinFlipperRepository"
    }

    var coinFlipperContract: Contract

    init {
        val contractValidator = context.assets.open("CoinFlipper.arc4.json")
            .bufferedReader()
            .use { it.readText() }
        coinFlipperContract = Encoder.decodeFromJson(contractValidator, Contract::class.java)
    }

    suspend fun appFlipCoin(
        account: Account,
        amount: Int,
        isHeads: Boolean,
    ): AtomicTransactionComposer.ExecuteResult? {
        var result: AtomicTransactionComposer.ExecuteResult?
        withContext(Dispatchers.IO) {
            val tws = createTransactionWithSigner(account, Constants.COINFLIP_APP_ID_TESTNET, amount)
            val methodArgs = listOf(tws, isHeads)
            val boxReferences = null
            val method = coinFlipperContract.getMethodByName("flip_coin")

            result = methodCallTransaction(
                appId = Constants.COINFLIP_APP_ID_TESTNET,
                account = account,
                method = method,
                methodArgs = methodArgs,
                boxReferences = boxReferences
            )
        }
        return result
    }

    suspend fun appSettleBet(
        account: Account
    ): String? {
        var result: AtomicTransactionComposer.ExecuteResult?
        withContext(Dispatchers.IO) {
            val randomBeaconApp = BigInteger.valueOf(Constants.RANDOM_BEACON_APPID)
            val methodArgs = listOf(account.address, randomBeaconApp)
            val boxReferences = null
            val method = coinFlipperContract.getMethodByName("settle")

            result = methodCallTransaction(
                appId = Constants.COINFLIP_APP_ID_TESTNET,
                account = account,
                method = method,
                methodArgs = methodArgs,
                boxReferences = boxReferences
            )
        }
        if (result == null || result?.confirmedRound == null || result?.methodResults == null) {
            return null
        } else {
            val betResult = result?.methodResults?.get(0)?.value as Array<*>
            return createAlertMessage(betResult)
        }
    }

    fun createAlertMessage(result: Array<*>): String {
        val outcome = if (result.get(0) as Boolean == true) "Won!" else "Lost :("
        val amount = result.get(1) as BigInteger
        val msg = "You $outcome  ($amount)"
        return(msg)
    }
}
