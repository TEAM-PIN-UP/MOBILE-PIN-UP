package com.pinup.pinup.ui.login.sns.controller

import android.content.Context
import com.pinup.pinup.ui.login.sns.SNSLoginResultListener

interface SNSLoginController {
    fun doLogin(
        context: Context,
        resultListener: SNSLoginResultListener
    )
}
