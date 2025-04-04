package com.pinup.pinup.ui.login.sns

interface NaverLoginController {
    fun doLogin(
        resultListener: SNSLoginResultListener,
        context: Any,
    )
}