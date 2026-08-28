package com.pinup.placePinup.data.response

import com.pinup.placePinup.domain.model.Review
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val id: Int,
    val placeName: String,
    val kakaoPlaceId: String? = null,
    val content: String,
    val createdAt: List<Int> = emptyList(),
    val starRating: Double,
    val writerProfileImageUrl: String?,
    val reviewImageUrls: List<String>?,
    val isOwn: Boolean,
    val likeCount: Int,
    val isLikedByUser: Boolean,
    val commentCount: Int,
    val isScrapByUser: Boolean,
    val writerName: String,
) {
    companion object {
        fun ReviewResponse.toModel(): Review {
            return Review(
                id = id,
                placeName = placeName,
                kakaoPlaceId = kakaoPlaceId ?: "",
                content = content,
                createdAt = createdAt,
                starRating = starRating,
                writerProfileImageUrl = writerProfileImageUrl ?: "",
                reviewImageUrls = reviewImageUrls,
                isOwn = isOwn,
                likeCount = likeCount,
                isLikedByUser = isLikedByUser,
                commentCount = commentCount,
                isScrapByUser = isScrapByUser,
                writerName = writerName
            )
        }
    }
}