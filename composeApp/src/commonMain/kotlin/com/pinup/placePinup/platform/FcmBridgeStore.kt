package com.pinup.placePinup.platform

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object FcmBridgeStore {
    private val _pending = MutableStateFlow<Pair<String, Int>?>("" to -1)
    val pending = _pending.asStateFlow()
    private var fcmToken: String = ""

    fun setFcmToken(token: String) {
        fcmToken = token
    }

    fun getFcmToken() = fcmToken

    fun updatePending(type: String, id: Int) {
        _pending.update { type to id }
    }

    fun consume() {
        _pending.value = null
    }
}