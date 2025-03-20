package com.pinup.pinup.extentions

fun Boolean.toYnString(): String {
    return if (this) {
        "Y"
    } else {
        "N"
    }
}