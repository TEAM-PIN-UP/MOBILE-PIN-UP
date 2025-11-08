package com.pinup.placePinup.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object ScreenState {
    private val _errorMessage = MutableSharedFlow<String>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val errorMessage: SharedFlow<String>
        get() = _errorMessage.asSharedFlow()

    fun updateError(error: String)  {
        _errorMessage.tryEmit(error)
    }
}