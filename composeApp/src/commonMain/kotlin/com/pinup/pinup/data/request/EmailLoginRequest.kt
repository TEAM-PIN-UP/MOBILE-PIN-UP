package com.pinup.pinup.data.request

import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginRequest(
    val email : String,
    val password: String
)
