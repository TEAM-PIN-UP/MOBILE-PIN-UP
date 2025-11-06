package com.pinup.placePinup.util

fun formatWithCommas(value: Int): String {
    val negative = value < 0
    var n = kotlin.math.abs(value)
    val sb = StringBuilder()
    var group = 0

    if (n == 0) {
        sb.append('0')
    }
    while (n > 0) {
        val d = (n % 10).toInt()
        sb.append(('0'.code + d).toChar())
        n /= 10
        group++
        if (n > 0 && group == 3) {
            sb.append(',')
            group = 0
        }
    }
    if (negative) sb.append('-')
    return sb.reverse().toString()
}