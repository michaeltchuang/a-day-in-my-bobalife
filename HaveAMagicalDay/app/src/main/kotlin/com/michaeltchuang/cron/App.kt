@file:JvmName("App")

package com.michaeltchuang.cron

import com.michaeltchuang.cron.data.AlgorandRepository
import com.michaeltchuang.cron.utils.Constants
import com.michaeltchuang.cron.utils.DateUtils
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.TimeZone

val tz = TimeZone.getTimeZone("America/Los_Angeles")
val dateFormatter: DateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(tz.toZoneId())
val currentDateStr = LocalDate.now(tz.toZoneId()).format(dateFormatter).toString()

fun main() {
    val repository = AlgorandRepository()
    val account = repository.recoverAccount(Constants.TEST_PASSPHRASE)

    if (account == null) {
        throw Exception("Could not find account")
    } else {
        val volumeNum = DateUtils().calculateVolumeNum(false, Constants.VOL2_START_DATE, currentDateStr)
        var vlogNum = DateUtils().calculateVlogNum(false, Constants.VOL2_START_DATE, currentDateStr)
        account.address?.apply { AppendToCsvFile(vlogNum, repository.sendPayment(account, Constants.COINFLIP_APP_ID_TESTNET,
            Constants.DEFAULT_MICRO_ALGO_TRANSFER_AMOUNT, getGreeting(volumeNum, vlogNum))) }

        //send twice if Mondays - 8th vlog
        if(LocalDate.now(tz.toZoneId()).dayOfWeek.value == 1) {
            vlogNum = DateUtils().calculateVlogNum(true, Constants.VOL2_START_DATE, currentDateStr)
            account.address?.apply { AppendToCsvFile(vlogNum, repository.sendPayment(account, Constants.COINFLIP_APP_ID_TESTNET,
                Constants.DEFAULT_MICRO_ALGO_TRANSFER_AMOUNT, getGreeting(volumeNum, vlogNum))) }
        }
    }
}

fun getGreeting(volume: Int, vlogNum: Int) : String {
    val note = "Vlog ${volume}-${vlogNum}, have a magical day everyone! - Michael T Chuang"
    println(note)
    return note
}

fun AppendToCsvFile(vlogNum: Int, txnId: String?) {
    try {
        if(txnId != null) {
            val str = "\n${currentDateStr},2-${vlogNum},${txnId}"
            //println(Paths.get("").toAbsolutePath().toString())
            Files.write(
                Paths.get(Constants.HISTORY_CSV_FILE),
                str.toByteArray(),
                StandardOpenOption.APPEND
            )
        } else {
            throw IllegalStateException("Error in creating Algorand transaction")
        }
    } catch (e: Exception) { println(e.toString())}
}
