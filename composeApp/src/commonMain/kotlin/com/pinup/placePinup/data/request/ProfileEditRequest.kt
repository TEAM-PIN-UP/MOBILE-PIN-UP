package com.pinup.placePinup.data.request

import kotlinx.serialization.Serializable

@Serializable
data class ProfileEditRequest(
    val nickname: String = "",
    val bio: String = "",
    val profileImageUrl: String = ""
)