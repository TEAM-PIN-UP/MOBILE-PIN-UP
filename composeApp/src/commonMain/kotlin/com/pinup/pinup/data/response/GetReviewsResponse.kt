package com.pinup.pinup.data.response

import com.pinup.pinup.data.response.ReviewResponse.Companion.toModel
import com.pinup.pinup.domain.model.PagingReview
import kotlinx.serialization.Serializable

@Serializable
data class GetReviewsResponse(
    val reviews: List<ReviewResponse>,
    val hasNext: Boolean,
    val nextCursor: Int
) {
    companion object {
        fun GetReviewsResponse.toModel() : PagingReview {
            return PagingReview(
                reviews = reviews.map {
                    it.toModel()
                },
                hasNext = hasNext,
                nextCursor = nextCursor
            )
        }
    }
}