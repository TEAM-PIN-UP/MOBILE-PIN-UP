package com.pinup.pinup.data.request.pinlog

import com.pinup.pinup.data.request.PlaceRequest
import com.pinup.pinup.data.request.ReviewRequest
import kotlinx.serialization.Serializable

@Serializable
data class AddReviewRequest(
    val reviewRequest: ReviewRequest,
    val placeRequest: PlaceRequest
)
