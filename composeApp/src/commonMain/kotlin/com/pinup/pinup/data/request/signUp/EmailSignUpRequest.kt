package com.pinup.pinup.data.request.signUp

import com.pinup.pinup.ui.login.model.SNSType
import kotlinx.serialization.Serializable

@Serializable
data class EmailSignUpRequest(
    val email: String,
    val password : String,
    val name: String,
    val nickname: String,
    val loginType: SNSType,
    val termsOfMarketing: String,
)