package com.pinup.placePinup

import com.pinup.placePinup.platform.FcmBridgeStore

class FCMLinkBridge {

    fun setFcmIos(token: String) {
        FcmBridgeStore.setFcmToken(token)
    }

    fun updateTargetId(id: Int) {
        FcmBridgeStore.updateTargetId(id)
    }

    fun updateType(type: String) {
        FcmBridgeStore.updateType(type)
    }
}