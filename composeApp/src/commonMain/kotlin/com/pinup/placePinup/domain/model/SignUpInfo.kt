package com.pinup.placePinup.domain.model

import com.pinup.placePinup.ui.login.model.SNSType

data class SignUpInfo(
    val email: String,
    val password: String = "",
    val name: String = "",
    val nickname: String,
    val socialId: String = "",
    val loginType: SNSType,
    val termsOfMarketing: Boolean,
    val profileImageUrl: String? = ""
)
