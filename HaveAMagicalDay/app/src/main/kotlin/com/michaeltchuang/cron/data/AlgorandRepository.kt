package com.michaeltchuang.cron.data

import com.algorand.algosdk.account.Account

import com.algorand.algosdk.v2.client.common.AlgodClient

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
}
