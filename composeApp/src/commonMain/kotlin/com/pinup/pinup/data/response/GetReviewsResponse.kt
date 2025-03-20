package com.pinup.pinup.data.response

import com.pinup.pinup.data.response.ReviewResponse.Companion.toModel
import com.pinup.pinup.domain.model.PagingReview
import kotlinx.serialization.Serializable

@Serializable
data class GetReviewsResponse(
    val content: List<ReviewResponse>,
    val totalElements: Int,
    val totalPages: Int
) {
    companion object {
        fun GetReviewsResponse.toModel() : PagingReview {
            return PagingReview(
                reviews = content.map {
                    it.toModel()
                },
                totalPages = totalPages,
                totalElements = totalElements
            )
        }
    }
}