package com.pinup.placePinup.platform

import com.pinup.placePinup.ui.login.sns.SNSLoginController
import com.pinup.placePinup.ui.login.sns.SNSLoginResultListener

expect class GoogleLoginController() : SNSLoginController {
    override fun doLogin(
        resultListener: SNSLoginResultListener,
        context: Any
    )
}
