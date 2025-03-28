package com.pinup.pinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val address: String,
    val averageStarRating: Double,
    val categoryCode: String,
    val description: String,
    val kakaoPlaceId: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val placeCategory: String,
    val reviewCount: Int,
    val roadAddress: String
)