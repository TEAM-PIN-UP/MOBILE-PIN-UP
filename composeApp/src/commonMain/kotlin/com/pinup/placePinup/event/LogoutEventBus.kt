package com.pinup.placePinup.event

import kotlinx.coroutines.flow.MutableSharedFlow

object LogoutEventBus {
    val logoutEvent = MutableSharedFlow<String?>()

    suspend fun sendEvent(message: String? = null) {
        logoutEvent.emit(message)
    }
}