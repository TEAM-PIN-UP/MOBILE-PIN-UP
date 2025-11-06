package com.pinup.placePinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenInfo(
    val accessToken: String,
    val refreshToken: String
)
