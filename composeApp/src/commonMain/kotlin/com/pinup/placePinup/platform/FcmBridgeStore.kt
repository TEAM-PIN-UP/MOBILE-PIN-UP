package com.pinup.placePinup.platform

object FcmBridgeStore {
    private var fcmToken: String = ""

    fun setFcmToken(token: String) {
        fcmToken = token
    }

    fun getFcmToken() = fcmToken
}