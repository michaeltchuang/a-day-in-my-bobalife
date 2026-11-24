package com.michaeltchuang.cron

import com.michaeltchuang.cron.utils.HistoryUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Paths

class HistoryUtilsTest {
    private fun historyFileWith(vararg lines: String) =
        Files.createTempFile("greeting-history", ".log").also {
            Files.write(it, lines.toList())
            it.toFile().deleteOnExit()
        }

    @Test
    fun entryExistsForDate() {
        val file = historyFileWith("2026-11-21,3-677,TXN1", "2026-11-22,3-678,TXN2")
        assertTrue(HistoryUtils.hasEntryForDate("2026-11-22", file))
    }

    @Test
    fun noEntryForDate() {
        val file = historyFileWith("2026-11-21,3-677,TXN1", "2026-11-22,3-678,TXN2")
        assertFalse(HistoryUtils.hasEntryForDate("2026-11-23", file))
    }

    @Test
    fun missingFileHasNoEntry() {
        assertFalse(HistoryUtils.hasEntryForDate("2026-11-23", Paths.get("does-not-exist.log")))
    }
}
