package com.pinup.placePinup.ui.login.sns

interface NaverLoginController {
    fun doLogin(
        resultListener: SNSLoginResultListener,
        context: Any,
    )
}