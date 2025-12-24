package com.pinup.placePinup

import com.pinup.placePinup.platform.FcmBridgeStore

class FCMLinkBridge {

    fun setFcmIos(token: String) {
        FcmBridgeStore.setFcmToken(token)
    }

    fun updatePending(type: String, id: Int) {
        FcmBridgeStore.updatePending(type, id)
    }
}