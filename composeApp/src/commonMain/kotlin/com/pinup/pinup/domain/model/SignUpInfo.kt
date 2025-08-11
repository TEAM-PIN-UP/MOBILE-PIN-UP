package com.pinup.pinup.domain.model

import com.pinup.pinup.ui.login.model.SNSType

data class SignUpInfo(
    val email: String,
    val password: String = "",
    val name: String = "test",
    val nickname: String,
    val socialId: String = "",
    val loginType: SNSType,
    val termsOfMarketing: Boolean,
)
