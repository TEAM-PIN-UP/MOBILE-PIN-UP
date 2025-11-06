package com.pinup.placePinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WriteReview(
    val content: String,
    val starRating: Double,
    val visitedDate: String,
)
