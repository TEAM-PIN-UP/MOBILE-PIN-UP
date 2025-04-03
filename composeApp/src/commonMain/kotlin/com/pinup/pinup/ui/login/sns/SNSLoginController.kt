package com.pinup.pinup.ui.login.sns

interface SNSLoginController {
    fun doLogin(
        resultListener: SNSLoginResultListener
    )
}
