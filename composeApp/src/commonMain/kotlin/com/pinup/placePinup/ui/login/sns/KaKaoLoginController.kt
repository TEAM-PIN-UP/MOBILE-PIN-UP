package com.pinup.placePinup.ui.login.sns

interface KaKaoLoginController {
    fun doLogin(
        resultListener: SNSLoginResultListener,
        context: Any,
    )
}