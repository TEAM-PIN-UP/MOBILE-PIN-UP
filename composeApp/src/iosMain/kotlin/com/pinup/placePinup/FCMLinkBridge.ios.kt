package com.pinup.placePinup

import com.pinup.placePinup.platform.FcmBridgeStore
import com.pinup.placePinup.platform.hLog

class FCMLinkBridge {
    fun setFcmIos(token: String) {
        FcmBridgeStore.setFcmToken(token)
    }
}