package com.pinup.placePinup.data.request

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val email: String = "",
    val password: String = "",
)
