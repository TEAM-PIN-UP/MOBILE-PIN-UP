package com.pinup.pinup.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs

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

private val dtRegex = Regex(
    """^\s*(\d{4})-(\d{1,2})-(\d{1,2})\s+(\d{1,2}):(\d{1,2})(?::(\d{1,2}))?\s*$"""
)

/** 7일 미만: ~분전/시간전/일전, 7일 이상: yy.MM.dd */
fun relativeOrDate(
    input: String,
    now: Instant = Clock.System.now(),
    tz: TimeZone = TimeZone.currentSystemDefault()
): String {
    if (input.isEmpty()) return ""
    val m = dtRegex.matchEntire(input) ?: error("잘못된 날짜 형식: $input")
    val (y, mo, d, h, mi, sOpt) = m.destructured
    val s = sOpt.ifEmpty { "0" }

    val ldt = LocalDateTime(y.toInt(), mo.toInt(), d.toInt(), h.toInt(), mi.toInt(), s.toInt())
    val at = ldt.toInstant(tz)

    val isFuture = at > now
    val sec = abs((if (isFuture) at - now else now - at).inWholeSeconds)

    val DAY = 24L * 60 * 60
    if (sec >= 7L * DAY) {
        val date = ldt.date
        val yy  = (date.year % 100).toString().padStart(2, '0')
        val mm  = date.monthNumber.toString().padStart(2, '0')
        val dd  = date.dayOfMonth.toString().padStart(2, '0')
        return "$yy.$mm.$dd"
    }

    val suffix = "전"
    return when {
        sec < 60L      -> "방금 $suffix"
        sec < 3600L    -> "${sec / 60}분$suffix"
        sec < DAY      -> "${sec / 3600}시간$suffix"
        else           -> "${sec / DAY}일$suffix"
    }
}

fun relativeOrDate(
    input: List<Int>,
    now: Instant = Clock.System.now(),
    tz: TimeZone = TimeZone.currentSystemDefault()
): String {
    if (input.isEmpty()) return ""
    val ldt = LocalDateTime(input[0], input[1], input[2], input[3], input[4], input[5], input[6])
    val at  = ldt.toInstant(tz)

    val isFuture = at > now
    val sec = abs((if (isFuture) at - now else now - at).inWholeSeconds)

    val DAY = 24L * 60 * 60
    if (sec >= 7L * DAY) {
        val date = ldt.date
        val yy = (date.year % 100).toString().padStart(2, '0')
        val mm = date.monthNumber.toString().padStart(2, '0')
        val dd = date.dayOfMonth.toString().padStart(2, '0')
        return "$yy.$mm.$dd"
    }

    val suffix = "전"
    return when {
        sec < 60L   -> "방금 $suffix"
        sec < 3600L -> "${sec / 60}분$suffix"
        sec < DAY   -> "${sec / 3600}시간$suffix"
        else        -> "${sec / DAY}일$suffix"
    }
}