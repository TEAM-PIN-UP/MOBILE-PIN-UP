package com.pinup.placePinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceReview(
    val content: String,
    val reviewId: Int,
    val reviewImageUrls: List<String>,
    val starRating: Double,
    val visitedDate: String,
    val writerName: String,
    val writerProfileImageUrl: String?,
    val writerTotalReviewCount: Int,
    val isLikeByUser: Boolean = false,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isOwn: Boolean = false
)