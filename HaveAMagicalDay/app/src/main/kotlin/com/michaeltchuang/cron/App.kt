package com.michaeltchuang.cron

import com.algorand.algosdk.account.Account
import com.algorand.algosdk.builder.transaction.PaymentTransactionBuilder
import com.algorand.algosdk.crypto.Address
import com.algorand.algosdk.transaction.SignedTransaction
import com.algorand.algosdk.util.Encoder
import com.algorand.algosdk.v2.client.Utils
import com.algorand.algosdk.v2.client.common.AlgodClient
import com.algorand.algosdk.v2.client.model.PendingTransactionResponse
import com.algorand.algosdk.v2.client.model.TransactionParametersResponse
import com.michaeltchuang.cron.data.AlgorandRepository
import com.michaeltchuang.cron.utils.Constants


private val TAG: String = "AlgorandRepository"
    private var client: AlgodClient = AlgodClient(
        Constants.ALGOD_API_ADDR,
        Constants.ALGOD_PORT,
        Constants.ALGOD_API_TOKEN,
        Constants.ALGOD_API_TOKEN_KEY
    )

    val txHeaders = arrayOf("Content-Type")
    val txValues = arrayOf("application/x-binary")

    fun main() {
//        Security.removeProvider("BC")
//        Security.insertProviderAt(BouncyCastleProvider(), 0)
        val repository = AlgorandRepository()
        val account = repository.recoverAccount(Constants.TEST_PASSPHRASE)
        println()

        if(account == null) {
            throw Exception("Could not find account")
        } else {
            account.address?.apply { repository.sendPayment(account, Constants.COINFLIP_APP_ID_TESTNET, Constants.DEFAULT_MICRO_ALGO_TRANSFER_AMOUNT) }
        }
    }



