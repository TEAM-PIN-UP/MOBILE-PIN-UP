package com.pinup.placePinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EditorPintsDetail(
    val id: Int = -1,
    val title: String = "",
    val content: String = "",
    val pintsPlaceList: List<EditorPintsPlace> = emptyList()
)

@Serializable
data class EditorPintsPlace(
    val id: Int = -1,
    val kakaoPlaceId: String = "",
    val name: String = "",
    val address: String = "",
    val averageStarRating: Double = 0.0,
    val reviewCount: Int = 0,
    val distant: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val pintsPlaceCategory: Category = Category.NONE,
    val reviewImages: List<ReviewImage> = emptyList(),
    val reviewerProfileImages: List<ReviewImage> = emptyList(),
    val bookmark: Boolean = false,
)

@Serializable
data class ReviewImage(
    val id: Int = -1,
    val url: String = "",
)