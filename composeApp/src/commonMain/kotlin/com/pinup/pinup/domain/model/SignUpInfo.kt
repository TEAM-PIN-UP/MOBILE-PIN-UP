package com.pinup.pinup.domain.model

import com.pinup.pinup.ui.login.model.SNSType
import java.io.File

data class SignUpInfo(
    val email: String,
    val name: String,
    val nickname: String,
    val socialId: String,
    val loginType: SNSType,
    val profileImage: File,
    val termsOfMarketing: Boolean,
)
