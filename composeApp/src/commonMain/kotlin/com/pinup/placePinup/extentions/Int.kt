package com.pinup.placePinup.extentions

fun Int.twoDigit(): String {
    return this.toString().padStart(2, '0')
}
