package com.pinup.placePinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PintsDetail(
    val pintsId: Int = -1,
    val title: String = "",
    val content: String = "",
    val createdAt: String = "",
    val placeSummaries: List<PinchPlaceItem> = emptyList()
)

@Serializable
data class PinchPlaceItem(
    val kakaoPlaceId: String = "",
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
