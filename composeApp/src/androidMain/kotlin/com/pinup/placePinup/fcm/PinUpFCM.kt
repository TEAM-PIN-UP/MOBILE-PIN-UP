package com.pinup.placePinup.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pinup.placePinup.platform.hLog

class PinUpFCM : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        //TODO 메시지 받을 때 처리
        hLog(remoteMessage.data.toString())
    }
}