package com.michaeltchuang.cron

import com.michaeltchuang.cron.data.AlgorandRepository
import com.michaeltchuang.cron.utils.Constants

fun main() {
    val repository = AlgorandRepository()
    val account = repository.recoverAccount(Constants.TEST_PASSPHRASE)
    println("Have a magical day everyone!")
    println("Account: ${account?.address ?: "Not Found"}")
}
