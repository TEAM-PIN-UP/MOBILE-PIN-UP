package com.pinup.placePinup.data.request.pinlog

import com.pinup.placePinup.data.request.PlaceRequest
import com.pinup.placePinup.data.request.ReviewRequest
import kotlinx.serialization.Serializable

@Serializable
data class AddReviewRequest(
    val reviewRequest: ReviewRequest,
    val placeRequest: PlaceRequest
)
