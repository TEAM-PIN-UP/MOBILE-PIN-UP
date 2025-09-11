package com.pinup.pinup.data.response

import com.pinup.pinup.domain.model.UserInfo
import kotlinx.serialization.Serializable

@Serializable
data class FindIdResponse(
    val email: String = "",
    val nickname: String = "",
    val profileUrl: String? = "",
) {
    companion object {
        fun FindIdResponse.toModel(): UserInfo {
            return UserInfo(
                email = email,
                nickname = nickname,
                profileUrl = profileUrl ?: ""
            )
        }
    }
}
