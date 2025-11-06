package com.pinup.placePinup.event

import kotlinx.coroutines.flow.MutableSharedFlow

object DetailPlaceEventBus {
    val detailPlaceEvent = MutableSharedFlow<String>()

    suspend fun sendEvent(kakaoPlaceId: String) {
        detailPlaceEvent.emit(kakaoPlaceId)
    }
}