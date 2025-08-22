package com.pinup.pinup.domain.model

data class PinlogDetail(
    val id: Int = 0,
    val placeName: String = "",
    val kakaoPlaceId: String = "",
    val content: String = "",
    val createdAt: String = "",
    val visitedDate: String = "",
    val starRating: Double = 0.0,
    val authorReviewCount: Int = 0,
    val writerProfileImageUrl: String = "",
    val reviewImageUrls: List<String> = emptyList(),
    val isOwn: Boolean = false,
    val likeCount: Int = 0,
    val isLikedByUser: Boolean = false,
    val commentCount: Int = 0,
    val comments: List<Comment> = emptyList(),
    val isScrapByUser: Boolean = false,
)