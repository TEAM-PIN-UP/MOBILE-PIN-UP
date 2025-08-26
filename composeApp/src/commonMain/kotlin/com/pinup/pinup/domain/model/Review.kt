package com.pinup.pinup.domain.model

data class Review(
    val id: Int,
    val writerName: String = "",
    val placeName: String,
    val content: String,
    val createdAt: String,
    val starRating: Double,
    val writerProfileImageUrl: String,
    val reviewImageUrls: List<String>?,
    val isOwn: Boolean,
    val likeCount: Int,
    val isLikedByUser: Boolean,
    val commentCount: Int,
    val isScrapByUser: Boolean,
)