package com.pinup.placePinup.ui.login.sns

interface AppleLoginController {
    fun doLogin(
        resultListener: SNSLoginResultListener,
        context: Any,
    )
}