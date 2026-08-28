package com.pinup.placePinup.event

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object DetailPlaceEventBus {
    val detailPlaceEvent = MutableSharedFlow<String>()

    /**
     * 지도 밖(핀로그 상세, 피드/마이/유저프로필 장소 뱃지 등)에서 특정 장소로 지도를 센터링해달라고 요청한 kakaoPlaceId.
     * 지도 진입 시 MapRoute 가 소비하며, 소비되기 전까지 값이 유지되므로
     * 탭 전환으로 MapViewModel 이 새로 만들어져도 요청이 유실되지 않는다.
     */
    private val _focusPlace = MutableStateFlow<String?>(null)
    val focusPlace = _focusPlace.asStateFlow()

    /**
     * 진입시킨 피드 리뷰의 식별자(친구 게이트 판별용).
     * 지도 상세 리뷰 목록에 이 id가 없으면(백엔드가 비친구 핀로그를 필터링) 비친구로 판단해 핀버디 신청을 유도한다.
     * 뱃지 외 경로(핀로그 상세 등) 진입 시 -1.
     */
    private val _focusReviewId = MutableStateFlow(-1)
    val focusReviewId = _focusReviewId.asStateFlow()

    suspend fun sendEvent(kakaoPlaceId: String) {
        detailPlaceEvent.emit(kakaoPlaceId)
    }

    fun requestFocusPlace(kakaoPlaceId: String, reviewId: Int = -1) {
        _focusReviewId.value = reviewId
        _focusPlace.value = kakaoPlaceId
    }

    fun consumeFocusPlace() {
        _focusPlace.value = null
        _focusReviewId.value = -1
    }
}
