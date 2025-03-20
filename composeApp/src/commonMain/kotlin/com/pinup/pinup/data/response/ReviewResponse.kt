package com.pinup.pinup.data.response

import com.pinup.pinup.domain.model.Review
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    val content: String,
    val createdAt: String,
    val kakaoPlaceId: String,
    val placeName: String,
    val reviewId: Int,
    val reviewImageUrls: List<String>? = null,
    val starRating: Double,
    val visitedDate: String
) {
    companion object {
        fun ReviewResponse.toModel(): Review {
            return Review(
                content = content,
                createdAt = createdAt,
                kakaoPlaceId = kakaoPlaceId,
                placeName = placeName,
                reviewId = reviewId,
                reviewImageUrls = reviewImageUrls,
                starRating = starRating,
                visitedDate = visitedDate,
            )
        }
    }
}