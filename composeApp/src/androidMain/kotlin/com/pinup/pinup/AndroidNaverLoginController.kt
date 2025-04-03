package com.pinup.pinup

import android.content.Context
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthErrorCode
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.login.sns.NaverLoginController
import com.pinup.pinup.ui.login.sns.SNSLoginResultListener
import org.koin.java.KoinJavaComponent

class AndroidNaverLoginController: NaverLoginController {
    override fun doLogin(resultListener: SNSLoginResultListener) {
        val context: Context = KoinJavaComponent.getKoin().get()
        NaverIdLoginSDK.authenticate(context, NaverLoginCallback(resultListener))
    }

    inner class NaverLoginCallback(
        private val resultListener: SNSLoginResultListener
    ) : OAuthLoginCallback {
        private fun handleError(errorCode: Int, message: String) {
            val code = NaverIdLoginSDK.getLastErrorCode().code

            if (code == NidOAuthErrorCode.CLIENT_USER_CANCEL.code) {
                resultListener.onCancel()
            } else {
                if (errorCode == -1) {
                    resultListener.onCancel()
                } else {
                    resultListener.onFail("errorCode: $errorCode, errorDesc: $message")
                }
            }
        }

        override fun onSuccess() {
            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onError(errorCode: Int, message: String) {
                    handleError(errorCode, message)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    resultListener.onCancel()
                }

                override fun onSuccess(result: NidProfileResponse) {
                    val snsLoginInfo = SNSUserInfo(
                        socialId = result.profile?.id ?: "",
                        snsType = SNSType.NAVER,
                        email = result.profile?.email,
                        name = result.profile?.name,
                        nickname = result.profile?.nickname
                    )
                    resultListener.onSuccess(snsLoginInfo)
                }
            })
        }

        override fun onError(errorCode: Int, message: String) {
            handleError(errorCode, message)
        }

        override fun onFailure(httpStatus: Int, message: String) {
            resultListener.onCancel()
        }
    }
}