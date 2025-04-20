@file:JvmName("App")

package com.michaeltchuang.cron

import com.algorand.algosdk.account.Account
import com.algorand.algosdk.crypto.Address
import com.michaeltchuang.cron.data.AlgorandRepository
import com.michaeltchuang.cron.utils.Constants
import com.michaeltchuang.cron.utils.DateUtils
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.TimeZone

object AppConfiguration {
    val tz: TimeZone = TimeZone.getTimeZone("America/Los_Angeles")
    val dateFormatter: DateTimeFormatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd")
        .withZone(tz.toZoneId())
}

class PaymentProcessor(
    private val repository: AlgorandRepository,
    private val dateUtils: DateUtils = DateUtils()
) {
    fun processPayments() {
        val account = repository.recoverAccount(System.getenv("CLIKT_PASSPHRASE"))
            ?: throw Exception("Could not find account")

        val currentDateStr = getDateString()
        val volumeNum = calculateVolumeNumber(currentDateStr)

        // First payment
        sendPaymentAndRecord(account, volumeNum, calculateVlogNumber(false, currentDateStr))

        // Send second payment if it's Monday
        if (isMonday()) {
            sendPaymentAndRecord(
                account,
                volumeNum,
                calculateVlogNumber(true, currentDateStr)
            )
        }
    }

    private fun sendPaymentAndRecord(account: Account, volumeNum: Int, vlogNum: Int) {
        account.address?.let { address ->
            val txnId = repository.sendPayment(
                account,
                "${Constants.RECEIVER_ADDRESS}",
                Constants.DEFAULT_MICRO_ALGO_TRANSFER_AMOUNT,
                createGreeting(volumeNum, vlogNum)
            )
            appendToCsvFile(volumeNum, vlogNum, txnId)
        }
    }

    private fun calculateVolumeNumber(currentDateStr: String): Int =
        dateUtils.calculateVolumeNum(false, Constants.VOL3_VLOG0_START_DATE, currentDateStr)

    private fun calculateVlogNumber(extra: Boolean, currentDateStr: String): Int =
        dateUtils.calculateVlogNum(extra, Constants.VOL3_VLOG0_START_DATE, currentDateStr)

    private fun isMonday(): Boolean =
        LocalDate.now(AppConfiguration.tz.toZoneId()).dayOfWeek.value == 1

    private fun createGreeting(volume: Int, vlogNum: Int): String {
        return "Vlog $volume-$vlogNum, have a magical day everyone! - Michael T Chuang".also {
            println(it)
        }
    }

    private fun appendToCsvFile(volume: Int, vlogNum: Int, txnId: String?) {
        try {
            txnId?.let {
                val entry = "${getDateString()},$volume-$vlogNum,$it\n"
                Files.write(
                    Paths.get(Constants.HISTORY_CSV_FILE),
                    entry.toByteArray(),
                    StandardOpenOption.APPEND
                )
            } ?: throw IllegalStateException("Error in creating Algorand transaction")
        } catch (e: Exception) {
            println(e.toString())
        }
    }
}

fun getDateString(daysAgo: Long = 0): String {
    return LocalDate.now(AppConfiguration.tz.toZoneId())
        .minusDays(daysAgo)
        .format(AppConfiguration.dateFormatter)
}

fun main() {
    PaymentProcessor(AlgorandRepository()).processPayments()
}
