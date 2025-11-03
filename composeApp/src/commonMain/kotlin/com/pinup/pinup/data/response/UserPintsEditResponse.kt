package com.pinup.pinup.data.response

import kotlinx.serialization.Serializable

@Serializable
data class UserPintsEditResponse(
    val pintsId: Int = -1,
    val title: String = "",
    val content: String = "",
    val isMyPints: Boolean = false,
    val createAt: String = "",
)
