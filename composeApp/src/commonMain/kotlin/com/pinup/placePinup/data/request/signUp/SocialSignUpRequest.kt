package com.pinup.placePinup.data.request.signUp

import com.pinup.placePinup.ui.login.model.SNSType
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