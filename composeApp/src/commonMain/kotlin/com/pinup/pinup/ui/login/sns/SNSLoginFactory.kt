package com.pinup.pinup.ui.login.sns

import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.GoogleLoginController
import com.pinup.pinup.ui.login.model.SNSType

class SNSLoginFactory(
    private val kaKaoLoginController: KaKaoLoginController,
    private val naverLoginController: NaverLoginController,
    private val googleLoginController: GoogleLoginController,
) {
    fun doLogin(snsType: SNSType, contextFactory: ContextFactory, resultListener: SNSLoginResultListener) {
        when (snsType) {
            SNSType.KAKAO -> kaKaoLoginController.doLogin(resultListener, contextFactory.getActivity())
            SNSType.NAVER -> naverLoginController.doLogin(resultListener, contextFactory.getActivity())
            SNSType.GOOGLE -> googleLoginController.doLogin(resultListener)
            SNSType.PINUP -> {}
        }
    }
}