package com.pinup.pinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReviewedPlace(
    val averageStarRating: Double = 0.0,
    val bookmark: Boolean = false,
    val distance: String? = "",
    val roadAddress: String = "",
    val kakaoPlaceId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val name: String = "",
    val placeCategory: Category = Category.NONE,
    val reviewCount: Int = 0,
    val reviewImageUrls: List<String> = emptyList(),
    val reviewerProfileImageUrls: List<String?> = emptyList(),
)
