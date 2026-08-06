package com.pinup.placePinup.event

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// 앱 레벨 푸시 화면(유저 프로필 등)에서 하단 Map 탭으로 전환을 요청하기 위한 신호.
// 값이 유지(StateFlow)되므로, Main 이 back stack 에서 다시 구성될 때 MainNavHost 가 읽어
// Map 탭으로 이동한 뒤 consume() 한다. (장소 상세 자체는 DetailPlaceEventBus 가 담당)
object MapTabBridge {
    private val _pending = MutableStateFlow(false)
    val pending = _pending.asStateFlow()

    fun request() {
        _pending.value = true
    }

    fun consume() {
        _pending.value = false
    }
}
