package com.pinup.placePinup.data.request.pints

import com.pinup.placePinup.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class ModifyPintsRequest(
    val title: String,
    val content: String,
    val placeRequests: List<PintsPlaceRequest>,
)

@Serializable
data class PintsPlaceRequest(
    val kakaoPlaceId: String,
    val name: String,
    val category: Category,
    val address: String,
    val roadAddress: String,
    val latitude: Double,
    val longitude: Double,
)