package com.pinup.pinup.extentions

fun Int.twoDigit(): String {
    return this.toString().padStart(2, '0')
}
