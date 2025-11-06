package com.pinup.placePinup.ui.login.sns

interface SNSLoginController {
    fun doLogin(
        resultListener: SNSLoginResultListener
    )
}
