package com.pinup.pinup.data.request.signUp

import com.pinup.pinup.ui.login.model.SNSType
import kotlinx.serialization.Serializable

@Serializable
data class SocialSignUpRequest(
    val email: String,
    val name: String,
    val nickname: String,
    val socialId: String,
    val loginType: SNSType,
    val termsOfMarketing: String,
    val profileImageUrl: String,
)