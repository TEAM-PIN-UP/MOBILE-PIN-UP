package com.pinup.pinup.data.request.pints

import kotlinx.serialization.Serializable

@Serializable
data class ModifyPintsRequest(
    val title: String,
    val content: String,
    val placeSummaries: List<PlaceSummariesRequest>
)

@Serializable
data class PlaceSummariesRequest(
    val kakaoPlaceId: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)