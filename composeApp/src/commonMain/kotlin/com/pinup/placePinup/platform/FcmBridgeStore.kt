package com.pinup.placePinup.platform

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration

object FcmBridgeStore {
    private val _pending = MutableStateFlow<Pair<String, Int>?>("" to -1)
    val pending = _pending.asStateFlow()

    // Android 는 onNewToken(백그라운드 스레드), iOS 는 Messaging delegate 에서 채워지므로 StateFlow 로 둔다.
    private val fcmToken = MutableStateFlow("")

    fun setFcmToken(token: String) {
        fcmToken.value = token
    }

    fun getFcmToken() = fcmToken.value

    /**
     * 토큰이 아직 발급 전이면(콜드 스타트 직후 등) [timeout] 까지 기다린다.
     * 끝내 받지 못하면 null.
     */
    suspend fun awaitFcmToken(timeout: Duration): String? =
        withTimeoutOrNull(timeout) { fcmToken.first { it.isNotBlank() } }

    fun updatePending(type: String, id: Int) {
        _pending.update { type to id }
    }

    fun consume() {
        _pending.value = null
    }
}
