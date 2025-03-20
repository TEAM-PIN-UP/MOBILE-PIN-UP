package com.pinup.pinup.data.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val memberResponse: MemberResponse,
    val refreshToken: String
)
