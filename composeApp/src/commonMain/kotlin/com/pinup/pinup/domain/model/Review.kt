package com.pinup.pinup.domain.model

data class Review(
    val content: String,
    val createdAt: String,
    val kakaoPlaceId: String,
    val placeName: String,
    val reviewId: Int,
    val reviewImageUrls: List<String>?,
    val starRating: Double,
    val visitedDate: String
)