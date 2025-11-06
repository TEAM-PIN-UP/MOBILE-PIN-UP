package com.pinup.placePinup.data.response

import com.pinup.placePinup.data.response.ReviewResponse.Companion.toModel
import com.pinup.placePinup.domain.model.PagingReview
import kotlinx.serialization.Serializable

@Serializable
data class GetReviewsResponse(
    val reviews: List<ReviewResponse>,
    val hasNext: Boolean,
    val nextCursor: Int?
) {
    companion object {
        fun GetReviewsResponse.toModel() : PagingReview {
            return PagingReview(
                reviews = reviews.map {
                    it.toModel()
                },
                hasNext = hasNext,
                nextCursor = nextCursor ?: 0
            )
        }
    }
}