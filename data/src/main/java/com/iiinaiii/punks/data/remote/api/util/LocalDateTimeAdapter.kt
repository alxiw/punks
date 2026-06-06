package com.iiinaiii.punks.data.remote.api.util

import org.threeten.bp.Year
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeParseException
import org.threeten.bp.format.TextStyle
import java.util.Locale

internal object LocalDateTimeAdapter {

    private val MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MM/yyyy")
    private val YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy")

    fun formatDate(string: String): String {
        return try {
            YearMonth.parse(string, MONTH_YEAR_FORMATTER).run {
                "${month.getDisplayName(TextStyle.FULL, Locale.US).uppercase(Locale.US)} $year"
            }
        } catch (e: DateTimeParseException) {
            try {
                Year.parse(string, YEAR_FORMATTER).run {
                    toString()
                }
            } catch (e: DateTimeParseException) {
                string
            }
        }
    }
}
