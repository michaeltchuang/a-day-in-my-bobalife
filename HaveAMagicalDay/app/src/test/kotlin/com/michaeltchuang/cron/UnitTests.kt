package com.michaeltchuang.cron

import com.michaeltchuang.cron.utils.Constants
import com.michaeltchuang.cron.utils.Constants.VOL2_VLOG1_DATE
import com.michaeltchuang.cron.utils.Constants.VOL2_VLOG22_DATE
import com.michaeltchuang.cron.utils.Constants.VOL2_VLOG23_DATE
import com.michaeltchuang.cron.utils.Constants.VOL2_VLOG24_DATE
import com.michaeltchuang.cron.utils.Constants.VOL2_VLOG25_DATE
import com.michaeltchuang.cron.utils.Constants.VOL2_VLOG31_DATE
import com.michaeltchuang.cron.utils.Constants.VOL2_VLOG39_DATE
import com.michaeltchuang.cron.utils.Constants.VOL2_VLOG42_DATE
import com.michaeltchuang.cron.utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitTests {
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
