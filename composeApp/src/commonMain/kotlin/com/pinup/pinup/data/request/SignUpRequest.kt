package com.pinup.pinup.data.request

import com.pinup.pinup.ui.login.model.SNSType
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val name: String,
    val nickname: String,
    val socialId: String,
    val loginType: SNSType,
    val termsOfMarketing: String,
)
