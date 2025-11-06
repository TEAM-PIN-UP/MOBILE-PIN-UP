package com.pinup.placePinup.data.response

import kotlinx.serialization.Serializable

@Serializable
data class SingUpResponse(
    val accessToken: String,
    val refreshToken: String
)
