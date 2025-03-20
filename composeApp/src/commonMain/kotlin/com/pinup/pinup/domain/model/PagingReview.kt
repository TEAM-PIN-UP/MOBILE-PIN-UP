package com.pinup.pinup.domain.model

data class PagingReview(
    val reviews: List<Review>,
    val totalElements: Int,
    val totalPages: Int
)