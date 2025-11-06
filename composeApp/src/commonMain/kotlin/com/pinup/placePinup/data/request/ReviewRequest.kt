package com.pinup.placePinup.data.request

import com.pinup.placePinup.domain.model.WriteReview
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    val content: String,
    val starRating: Double,
    val visitedDate: String?,
    val reviewImageUrls: List<String>,
) {
    companion object {
        fun of(writeReview: WriteReview): ReviewRequest {
            return ReviewRequest(
                content = writeReview.content,
                starRating = writeReview.starRating,
                visitedDate = writeReview.visitedDate,
                reviewImageUrls = emptyList()
            )
        }
    }
}