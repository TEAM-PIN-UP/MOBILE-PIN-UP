package com.pinup.pinup.ui.login.sns

import com.pinup.pinup.platform.GoogleLoginController
import com.pinup.pinup.ui.login.model.SNSType

class SNSLoginFactory(
    private val kaKaoLoginController: KaKaoLoginController,
    private val naverLoginController: NaverLoginController,
    private val googleLoginController: GoogleLoginController,
) {
    fun doLogin(snsType: SNSType, resultListener: SNSLoginResultListener) {
        when (snsType) {
            SNSType.KAKAO -> kaKaoLoginController.doLogin(resultListener)
            SNSType.NAVER -> naverLoginController.doLogin(resultListener)
            SNSType.GOOGLE -> googleLoginController.doLogin(resultListener)
        }
    }
}