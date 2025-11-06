package com.pinup.placePinup.ui.login.sns

import com.pinup.placePinup.ui.login.model.SNSUserInfo

interface SNSLoginResultListener {
    fun onCancel()
    fun onFail(message: String?)
    fun onSuccess(snsLoginInfo: SNSUserInfo)
}