package com.michaeltchuang.cron

import com.algorand.algosdk.v2.client.common.AlgodClient
import com.michaeltchuang.cron.data.AlgorandRepository
import com.michaeltchuang.cron.utils.Constants
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.TimeZone

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
    val repository = AlgorandRepository()
    val account = repository.recoverAccount(Constants.TEST_PASSPHRASE)

    if (account == null) {
        throw Exception("Could not find account")
    } else {
        account.address?.apply { repository.sendPayment(account, Constants.COINFLIP_APP_ID_TESTNET,
            Constants.DEFAULT_MICRO_ALGO_TRANSFER_AMOUNT, getGreeting(false)) }

        //send twice if Mondays - 8th vlog
        val tz = TimeZone.getTimeZone("America/Los_Angeles")
        if(LocalDate.now(tz.toZoneId()).dayOfWeek.value == 1) {
            account.address?.apply { repository.sendPayment(account, Constants.COINFLIP_APP_ID_TESTNET,
                Constants.DEFAULT_MICRO_ALGO_TRANSFER_AMOUNT, getGreeting(true)) }
        }
    }
}

fun getGreeting(isEighth: Boolean) : String {
    var vlogNum = calculateVlogNum(isEighth)

    val note = "Volume 2 Vlog ${vlogNum}, have a magical day everyone! - Michael T Chuang"
    println(note)
    return note
}

fun calculateVlogNum(isEighth: Boolean) : Int {
    val tz = TimeZone.getTimeZone("America/Los_Angeles")
    val dateFormatter: DateTimeFormatter =  DateTimeFormatter.ofPattern("yyyyMMdd").withZone(tz.toZoneId())
    val from = LocalDate.parse("20230220", dateFormatter)
    val to = LocalDate.now(tz.toZoneId())
    val period = Period.between(from, to)
    val weeks = period.days / 8
    println("${period.days} days is ${weeks} weeks since Feb 21, 2023")

    if(!isEighth) {
        return ((weeks * 8) + (period.days % 7))
    } else {
        return (weeks * 8) + 8
    }
}
