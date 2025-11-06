package com.pinup.placePinup.extentions

fun Boolean.toYnString(): String {
    return if (this) {
        "Y"
    } else {
        "N"
    }
}