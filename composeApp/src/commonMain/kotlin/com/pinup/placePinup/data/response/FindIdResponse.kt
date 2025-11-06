package com.pinup.placePinup.data.response

import com.pinup.placePinup.domain.model.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class FindIdResponse(
    val email: String = "",
    val nickname: String = "",
    val profileImageUrl: String? = "",
) {
    companion object {
        fun FindIdResponse.toModel(): UserInfo {
            return UserInfo(
                email = email,
                nickname = nickname,
                profileUrl = profileImageUrl ?: ""
            )
        }
    }
}
