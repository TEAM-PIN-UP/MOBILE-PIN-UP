package com.pinup.placePinup.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object DetailPlaceEventBus {
    val detailPlaceEvent = MutableSharedFlow<String>()

    /**
     * 지도 밖(핀로그 상세 등)에서 특정 장소로 지도를 센터링해달라고 요청한 kakaoPlaceId.
     * 지도 진입 시 MapRoute 가 소비하며, 소비되기 전까지 값이 유지되므로
     * 탭 전환으로 MapViewModel 이 새로 만들어져도 요청이 유실되지 않는다.
     */
    private val _focusPlace = MutableStateFlow<String?>(null)
    val focusPlace = _focusPlace.asStateFlow()

    suspend fun sendEvent(kakaoPlaceId: String) {
        detailPlaceEvent.emit(kakaoPlaceId)
    }

    fun requestFocusPlace(kakaoPlaceId: String) {
        _focusPlace.value = kakaoPlaceId
    }

    fun consumeFocusPlace() {
        _focusPlace.value = null
    }
}
