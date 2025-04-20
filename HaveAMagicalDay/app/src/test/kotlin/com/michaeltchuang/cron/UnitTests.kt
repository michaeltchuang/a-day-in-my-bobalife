package com.michaeltchuang.cron

import com.michaeltchuang.cron.utils.Constants
import com.michaeltchuang.cron.utils.Constants.VOL3_VLOG14_DATE
import com.michaeltchuang.cron.utils.Constants.VOL3_VLOG1_DATE
import com.michaeltchuang.cron.utils.Constants.VOL3_VLOG8_DATE
import com.michaeltchuang.cron.utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class UnitTests {
    @Test
    fun vlog1() {
        val output =
            DateUtils().calculateVlogNum(
                false,
                Constants.VOL3_VLOG0_START_DATE,
                VOL3_VLOG1_DATE,
            )
        assertEquals(output, 1)
    }

    @Test
    fun vlog8() {
        val output =
            DateUtils().calculateVlogNum(
                false,
                Constants.VOL3_VLOG0_START_DATE,
                VOL3_VLOG8_DATE,
            )
        // 8th vlog counts as 7th vlog
        assertEquals(7, output)
    }

    @Test
    fun vlog14() {
        val output =
            DateUtils().calculateVlogNum(
                false,
                Constants.VOL3_VLOG0_START_DATE,
                VOL3_VLOG14_DATE,
            )
        assertEquals(14, output)
    }
}
