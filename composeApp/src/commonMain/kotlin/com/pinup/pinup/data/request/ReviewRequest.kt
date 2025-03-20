package com.pinup.pinup.data.request

import com.pinup.pinup.domain.model.WriteReview
import kotlinx.serialization.Serializable

@Serializable
data class ReviewRequest(
    val content: String,
    val starRating: Double,
    val visitedDate: String
) {
    companion object {
        fun of(writeReview: WriteReview): ReviewRequest {
            return ReviewRequest(
                content = writeReview.content,
                starRating = writeReview.starRating,
                visitedDate = writeReview.visitedDate,
            )
        }
    }
}