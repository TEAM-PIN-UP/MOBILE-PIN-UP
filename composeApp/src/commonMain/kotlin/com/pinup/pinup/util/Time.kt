package com.pinup.pinup.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal val YEAR_RANGE = (1000..9999).toList()
internal val MONTH_RANGE = (1..12).toList()
internal val DATE_RANGE = (1..31).toList()

internal val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
internal val currentDate = currentDateTime.date
internal val currentYear = currentDateTime.year
internal val currentMonth = currentDateTime.monthNumber

enum class TimeFormat {
    HOUR_12, HOUR_24
}

enum class TimePeriod {
    AM, PM
}

fun toShortDateXd(iso: String): String {
    if (iso.isEmpty()) return ""
    val d = LocalDate.parse(iso.trim())
    return "${(d.year % 100).toString().padStart(2,'0')}." +
            "${d.monthNumber.toString().padStart(2,'0')}." +
            d.dayOfMonth.toString().padStart(2,'0')
}