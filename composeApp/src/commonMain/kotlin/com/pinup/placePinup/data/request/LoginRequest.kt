package com.pinup.placePinup.data.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val provider : String,
    val socialId: String
)
