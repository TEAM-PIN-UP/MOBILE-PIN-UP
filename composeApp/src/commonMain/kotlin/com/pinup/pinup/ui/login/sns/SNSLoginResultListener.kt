package com.pinup.pinup.ui.login.sns

import com.pinup.pinup.ui.login.model.SNSUserInfo

interface SNSLoginResultListener {
    fun onCancel()
    fun onFail(message: String?)
    fun onSuccess(snsLoginInfo: SNSUserInfo)
}