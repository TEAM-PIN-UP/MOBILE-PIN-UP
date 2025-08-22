package com.pinup.pinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReviewedPlace(
    val averageStarRating: Double,
    val bookmark: Boolean,
    val distance: String?,
    val roadAddress: String = "",
    val kakaoPlaceId: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val placeCategory: Category,
    val reviewCount: Int,
    val reviewImageUrls: List<String>,
    val reviewerProfileImageUrls: List<String?>
)
