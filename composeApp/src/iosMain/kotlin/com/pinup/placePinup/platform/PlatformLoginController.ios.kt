package com.pinup.placePinup.platform

import com.pinup.placePinup.ui.login.sns.SNSLoginResultListener
import com.pinup.placePinup.ui.login.sns.SNSLoginController
import cocoapods.GoogleSignIn.GIDSignIn
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.login.model.SNSUserInfo
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIApplication

actual class GoogleLoginController : SNSLoginController {
    @OptIn(ExperimentalForeignApi::class)
    override fun doLogin(resultListener: SNSLoginResultListener, context: Any,) {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController ?: run {
            resultListener.onCancel()
            return
        }

        GIDSignIn.sharedInstance.signInWithPresentingViewController(rootViewController) { result, error ->
            when {
                result != null -> {
                    resultListener.onSuccess(
                        SNSUserInfo(
                            socialId = result.user.userID ?: "",
                            name = result.user.profile?.name ?: "",
                            nickname = result.user.profile?.name ?: "",
                            email = result.user.profile?.email ?: "",
                            snsType = SNSType.GOOGLE
                        )
                    )
                }
                error != null -> {
                    resultListener.onFail(error.localizedDescription)
                }
                else -> {
                    resultListener.onCancel()
                }

            }
        }
    }
}