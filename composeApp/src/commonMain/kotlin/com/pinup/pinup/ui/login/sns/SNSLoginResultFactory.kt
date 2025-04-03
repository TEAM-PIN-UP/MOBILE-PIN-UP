package com.pinup.pinup.ui.login.sns

import com.pinup.pinup.platform.GoogleLoginController
import com.pinup.pinup.platform.KakaoLoginController
import com.pinup.pinup.platform.NaverLoginController
import com.pinup.pinup.ui.login.model.SNSType

object SNSLoginResultFactory {
    fun initialize(snsType: SNSType): SNSLoginController {
        return when (snsType) {
            SNSType.KAKAO -> KakaoLoginController()
            SNSType.NAVER -> NaverLoginController()
            SNSType.GOOGLE -> GoogleLoginController()
        }
    }
}