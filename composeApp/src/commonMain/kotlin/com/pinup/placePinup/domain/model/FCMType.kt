package com.pinup.placePinup.domain.model

enum class FCMType {
    PLACE, PINLOG, PINBUDDY, USER, MY_PROFILE, NONE;

    companion object {
        fun of(name: String): FCMType {
            return if (FCMType.entries.any { it.name == name }) {
                FCMType.valueOf(name)
            } else {
                FCMType.NONE
            }
        }
    }
}