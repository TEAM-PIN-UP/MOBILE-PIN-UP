package com.pinup.placePinup.platform

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object FcmBridgeStore {
    private val _targetId = MutableStateFlow<Int>(-1)
    val targetId = _targetId.asStateFlow()
    private val _type = MutableStateFlow<String>("")
    val type = _type.asStateFlow()
    private var fcmToken: String = ""

    fun setFcmToken(token: String) {
        fcmToken = token
    }

    fun getFcmToken() = fcmToken

    fun updateType(type: String) {
        _type.update { type }
    }

    fun updateTargetId(id: Int) {
        _targetId.update { id }
    }
}