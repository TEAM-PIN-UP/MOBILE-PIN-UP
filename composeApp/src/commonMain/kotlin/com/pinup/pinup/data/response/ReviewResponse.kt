package com.pinup.pinup.data.response

import com.pinup.pinup.domain.model.Review
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val id: Int,
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
) {
    companion object {
        fun ReviewResponse.toModel(): Review {
            return Review(
                id = id,
                placeName = placeName,
                content = content,
                createdAt = createdAt,
                starRating = starRating,
                writerProfileImageUrl = writerProfileImageUrl,
                reviewImageUrls = reviewImageUrls,
                isOwn = isOwn,
                likeCount = likeCount,
                isLikedByUser = isLikedByUser,
                commentCount = commentCount,
                isScrapByUser = isScrapByUser
            )
        }
    }
}