package com.pinup.placePinup.domain.model

import com.pinup.placePinup.ui.login.model.SNSType

data class UserInfo(
    val memberId: Int = -1,
    val profileUrl: String = "",
    val nickname: String = "",
    val name: String = "",
    val email: String = "",
    val snsType: SNSType = SNSType.KAKAO
)
