package com.pinup.placePinup.data.request.signUp

import com.pinup.placePinup.ui.login.model.SNSType
import kotlinx.serialization.Serializable

@Serializable
data class EmailSignUpRequest(
    val email: String,
    val password : String,
    val name: String,
    val nickname: String,
    val loginType: SNSType,
    val termsOfMarketing: String,
    val profileImageUrl: String
)