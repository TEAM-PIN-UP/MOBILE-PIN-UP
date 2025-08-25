package com.pinup.pinup.domain.model

data class PagingReview(
    val reviews: List<Review> = emptyList(),
    val hasNext: Boolean = false,
    val nextCursor: Int = 0,
)