package com.pinup.pinup.platform

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object KakaoDeepLinkStore {
    private val _params = MutableStateFlow<String>("-1")
    val params = _params.asStateFlow()

    fun onNewParams(userId: String) {
        _params.update { userId }
    }
}