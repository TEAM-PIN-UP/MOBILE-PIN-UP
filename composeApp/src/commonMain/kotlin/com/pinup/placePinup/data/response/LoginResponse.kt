package com.pinup.placePinup.data.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val memberResponse: MemberResponse,
    val refreshToken: String
)
