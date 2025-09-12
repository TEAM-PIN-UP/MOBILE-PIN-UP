package com.pinup.pinup.data.request.findId

import kotlinx.serialization.Serializable

@Serializable
data class FindByNickNameRequest(
    val nickname: String,
)
