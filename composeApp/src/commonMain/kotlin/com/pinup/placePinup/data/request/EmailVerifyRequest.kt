package com.pinup.placePinup.data.request

import kotlinx.serialization.Serializable

@Serializable
data class EmailVerifyRequest(
    val email: String,
    val code: String
)

@Serializable
data class SendVerifyCodeRequest(
    val email: String
)
