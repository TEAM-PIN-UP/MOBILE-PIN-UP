package com.pinup.placePinup.data.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterReviewResponse(
    val reviewId: String = ""
) {
    companion object {
        fun RegisterReviewResponse.toModel(): Int {
            return reviewId.toInt()
        }
    }
}
