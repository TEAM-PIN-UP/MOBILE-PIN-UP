package com.pinup.placePinup.ui.login.sns

import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.platform.GoogleLoginController
import com.pinup.placePinup.ui.login.model.SNSType

class SNSLoginFactory(
    private val kaKaoLoginController: KaKaoLoginController,
    private val naverLoginController: NaverLoginController,
    private val googleLoginController: GoogleLoginController,
    private val appleLoginController: AppleLoginController
) {
    fun doLogin(snsType: SNSType, contextFactory: ContextFactory, resultListener: SNSLoginResultListener) {
        when (snsType) {
            SNSType.KAKAO -> kaKaoLoginController.doLogin(resultListener, contextFactory.getActivity())
            SNSType.NAVER -> naverLoginController.doLogin(resultListener, contextFactory.getActivity())
            SNSType.GOOGLE -> googleLoginController.doLogin(resultListener, contextFactory.getActivity())
            SNSType.PINUP -> {}
            SNSType.APPLE -> appleLoginController.doLogin(resultListener, contextFactory.getActivity())
        }
    }
}