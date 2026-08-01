package com.pinup.placePinup.domain.model

data class Review(
    val id: Int,
    val writerName: String = "",
    val placeName: String,
    val kakaoPlaceId: String = "",
    val content: String,
    val createdAt: List<Int>,
    val starRating: Double,
    val writerProfileImageUrl: String,
    val reviewImageUrls: List<String>?,
    val isOwn: Boolean,
    val likeCount: Int,
    val isLikedByUser: Boolean,
    val commentCount: Int,
    val isScrapByUser: Boolean,
)