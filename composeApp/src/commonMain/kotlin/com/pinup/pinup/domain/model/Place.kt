package com.pinup.pinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val address: String = "",
    val averageStarRating: Double = 0.0,
    val categoryCode: String = "",
    val description: String = "",
    val kakaoPlaceId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val placeCategory: String = "",
    val reviewCount: Int = 0,
    val roadAddress: String = "",
    val image: String = "",
)