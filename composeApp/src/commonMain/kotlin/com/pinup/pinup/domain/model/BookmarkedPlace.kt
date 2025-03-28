package com.pinup.pinup.domain.model

data class BookmarkedPlace(
    val id: Int,
    val kakaoPlaceId: String,
    val placeAddress: String,
    val placeCategory: Category,
    val placeFirstReviewImageUrl: String,
    val placeId: Int,
    val placeLatitude: Double,
    val placeLongitude: Double,
    val placeName: String,
    val placeRoadAddress: String,
    val placeStatus: String
)
