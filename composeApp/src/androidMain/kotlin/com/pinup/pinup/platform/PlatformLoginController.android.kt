package com.pinup.pinup.platform

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthErrorCode
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.pinup.pinup.BuildConfig
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.login.sns.SNSLoginResultListener
import com.pinup.pinup.ui.login.sns.SNSLoginController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import java.util.Collections

actual class GoogleLoginController : SNSLoginController {
    override fun doLogin(resultListener: SNSLoginResultListener) {
        CoroutineScope(Dispatchers.IO).launch {
            val context: Context = KoinJavaComponent.getKoin().get()
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            val result = CredentialManager.create(context)
                .getCredential(
                    context = context,
                    request = request
                )

            // start credential
            val credentials = result.credential
            val googleIdTokenCredential = GoogleIdTokenCredential
                .createFrom(credentials.data)

            val googleIdToken = googleIdTokenCredential.idToken
            val verifier = GoogleIdTokenVerifier.Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance()
            )
                .setAudience(Collections.singletonList(BuildConfig.GOOGLE_CLIENT_ID))
                .build()
            val idToken = withContext(Dispatchers.IO) {
                verifier.verify(googleIdToken)
            }
            val payload = idToken.payload
            val snsLoginInfo = SNSUserInfo(
                socialId = payload.subject,
                snsType = SNSType.GOOGLE,
                email = payload.email,
                name = googleIdTokenCredential.givenName,
                nickname = googleIdTokenCredential.displayName
            )
            resultListener.onSuccess(snsLoginInfo)
        }
    }
}

actual class NaverLoginController : SNSLoginController {
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