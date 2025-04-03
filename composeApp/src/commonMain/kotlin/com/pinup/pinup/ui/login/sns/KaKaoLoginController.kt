package com.pinup.pinup.ui.login.sns

interface KaKaoLoginController {
    fun doLogin(
        resultListener: SNSLoginResultListener
    )
}