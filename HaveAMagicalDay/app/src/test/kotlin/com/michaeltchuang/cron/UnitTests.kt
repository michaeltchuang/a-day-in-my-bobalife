package com.michaeltchuang.cron

import com.michaeltchuang.cron.utils.Constants
import com.michaeltchuang.cron.utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitTests {
    val VOL2_VLOG1_DATE = "2023-02-21"
    val VOL2_VLOG22_DATE = "2023-03-12"
    val VOL2_VLOG23_DATE = "2023-03-13"
    val VOL2_VLOG24_DATE = "2023-03-13"
    val VOL2_VLOG25_DATE = "2023-03-14"
    val VOL2_VLOG31_DATE = "2023-03-20"
    val VOL2_VLOG39_DATE = "2023-03-27"
    val VOL2_VLOG42_DATE = "2023-03-29"

    @Test
    fun vlog1() {
        val output = DateUtils().calculateVlogNum(false, Constants.VOL2_START_DATE, VOL2_VLOG1_DATE)
        assertEquals(output, 1)
    }

    @Test
    fun vlog22() {
        val output = DateUtils().calculateVlogNum(false, Constants.VOL2_START_DATE, VOL2_VLOG22_DATE)
        assertEquals(22, output)
    }

    @Test
    fun vlog23() {
        val output = DateUtils().calculateVlogNum(false, Constants.VOL2_START_DATE, VOL2_VLOG23_DATE)
        assertEquals(23, output)
    }

    @Test
    fun vlog24() {
        val output = DateUtils().calculateVlogNum(true, Constants.VOL2_START_DATE, VOL2_VLOG24_DATE)
        assertEquals(24, output)
    }

    @Test
    fun vlog25() {
        val output = DateUtils().calculateVlogNum(false, Constants.VOL2_START_DATE, VOL2_VLOG25_DATE)
        assertEquals(25, output)
    }

    @Test
    fun vlog31() {
        val output = DateUtils().calculateVlogNum(false, Constants.VOL2_START_DATE, VOL2_VLOG31_DATE)
        assertEquals(31, output)
    }

    @Test
    fun vlog39() {
        val output = DateUtils().calculateVlogNum(false, Constants.VOL2_START_DATE, VOL2_VLOG39_DATE)
        assertEquals(39, output)
    }

    @Test
    fun vlog42() {
        val output = DateUtils().calculateVlogNum(false, Constants.VOL2_START_DATE, VOL2_VLOG42_DATE)
        assertEquals(42, output)
    }
}
