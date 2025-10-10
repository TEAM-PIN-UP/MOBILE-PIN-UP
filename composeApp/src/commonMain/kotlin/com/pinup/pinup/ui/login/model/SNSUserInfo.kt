package com.pinup.pinup.ui.login.model

import kotlinx.serialization.Serializable

@Serializable
data class SNSUserInfo(
    val socialId: String = "",
    val snsType: SNSType,
    val email: String? = "",
    val name: String? = "",
    val nickname: String? = ""
)