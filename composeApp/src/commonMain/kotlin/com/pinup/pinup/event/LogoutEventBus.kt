package com.pinup.pinup.event

import kotlinx.coroutines.flow.MutableSharedFlow

object LogoutEventBus {
    val logoutEvent = MutableSharedFlow<String?>()

    suspend fun sendEvent(message: String?) {
        logoutEvent.emit(message)
    }
}