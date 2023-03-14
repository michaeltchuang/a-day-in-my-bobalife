package com.michaeltchuang.cron.utils

import java.time.Duration
import java.time.LocalDateTime

class DateUtils() {
    fun calculateVlogNum(returnEighth: Boolean, startDateStr: String, endDateStr: String) : Int {
        val from = LocalDateTime.parse(startDateStr + Constants.DEFAULT_VLOG_TIME)
        val to = LocalDateTime.parse(endDateStr + Constants.DEFAULT_VLOG_TIME)
        val days = Duration.between(from, to).toDays()
        println("${days} days between ${startDateStr} and ${endDateStr}")

        if(returnEighth) {
            return (days / 7 * 8).toInt()
        } else if (days % 7 == 0L) {
            return ((days / 7 * 8) - 1).toInt()
        } else {
            return ((days / 7 * 8) + (days % 7)).toInt()
        }
    }

    fun calculateVolumeNum(returnEighth: Boolean, startDateStr: String, endDateStr: String) : Int {
        //hardcode to volume 2 for now
        return 2
    }
}