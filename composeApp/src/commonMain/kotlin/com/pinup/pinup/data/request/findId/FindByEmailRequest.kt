package com.pinup.pinup.data.request.findId

import kotlinx.serialization.Serializable

@Serializable
data class FindByEmailRequest(
    val email: String,
)