package com.pinup.placePinup.event

import kotlinx.coroutines.flow.MutableSharedFlow

// 피드 뱃지 탭 -> 핀맵 진입 시 전달되는 이벤트.
// reviewId: 진입시킨 피드 리뷰의 식별자(친구 게이트 판별용). 뱃지 외 경로 진입 시 -1.
data class DetailPlaceEvent(
    val kakaoPlaceId: String,
    val reviewId: Int = -1,
)

object DetailPlaceEventBus {
    val detailPlaceEvent = MutableSharedFlow<DetailPlaceEvent>()

    suspend fun sendEvent(kakaoPlaceId: String, reviewId: Int = -1) {
        detailPlaceEvent.emit(DetailPlaceEvent(kakaoPlaceId, reviewId))
    }
}
