package com.michaeltchuang.cron.utils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object HistoryUtils {
    /**
     * Returns true if the greeting history log already contains an entry for [dateStr]
     * (format yyyy-MM-dd). Used to avoid sending/logging a duplicate greeting when the
     * GitHub Action is re-run (e.g. manually) on a day it already ran.
     */
    fun hasEntryForDate(
        dateStr: String,
        historyFile: Path = Paths.get(Constants.HISTORY_CSV_FILE),
    ): Boolean {
        if (!Files.exists(historyFile)) return false
        return Files.newBufferedReader(historyFile).useLines { lines ->
            lines.any { it.substringBefore(',').trim() == dateStr }
        }
    }
}
