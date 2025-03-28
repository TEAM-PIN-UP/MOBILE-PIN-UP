package com.pinup.pinup.data.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val socialId: String
)
