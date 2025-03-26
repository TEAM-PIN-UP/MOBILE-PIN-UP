//package com.pinup.pinup.ui.login.sns.controller
//
//import android.content.Context
//import com.kakao.sdk.auth.model.OAuthToken
//import com.kakao.sdk.common.model.AuthError
//import com.kakao.sdk.common.model.AuthErrorCause
//import com.kakao.sdk.common.model.ClientError
//import com.kakao.sdk.common.model.ClientErrorCause
//import com.kakao.sdk.user.UserApiClient
//import com.pinup.pinup.ui.login.model.SNSType
//import com.pinup.pinup.ui.login.model.SNSUserInfo
//import com.pinup.pinup.ui.login.sns.SNSLoginResultListener
//import com.pinup.pinup.BuildConfig
//
//class KakaoLoginController : SNSLoginController {
//    override fun doLogin(
//        context: Context,
//        resultListener: SNSLoginResultListener
//    ) {
//        UserApiClient.instance.run {
//            if (isKakaoTalkLoginAvailable(context)) {
//                loginWithKakaoTalk(context) { token, error ->
//                    handleCallback(
//                        context = context,
//                        oAuthToken = token,
//                        throwable = error,
//                        resultListener = resultListener
//                    )
//                }
//            } else {
//                loginWithKakaoAccount(context) { token, error ->
//                    handleCallback(
//                        context = context,
//                        oAuthToken = token,
//                        throwable = error,
//                        resultListener = resultListener
//                    )
//                }
//            }
//        }
//    }
//
//    private fun handleCallback(
//        context: Context,
//        oAuthToken: OAuthToken?,
//        throwable: Throwable?,
//        resultListener: SNSLoginResultListener
//    ) {
//        if (throwable != null) {
//            if (throwable is AuthError && throwable.reason == AuthErrorCause.Unknown) {// 카카오톡이 깔려있지만 로그인 되어있지 않은 경우에 대한 예외처리
//                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
//                    handleCallback(
//                        context = context,
//                        oAuthToken = token,
//                        throwable = error,
//                        resultListener = resultListener
//                    )
//                }
//            } else {
//                val reason =
//                    when (throwable) {
//                        is ClientError -> "${throwable.message} (${throwable.reason.name})"
//                        is AuthError -> if (BuildConfig.DEBUG) "${throwable.message} (${throwable.reason.name})" else null
//                        else -> null
//                    }
//                if (throwable is ClientError && throwable.reason == ClientErrorCause.Cancelled) {
//                    resultListener.onCancel()
//                } else {
//                    resultListener.onFail(reason)
//                }
//            }
//        } else if (oAuthToken != null) {
//            // 토큰 정보 보기
//            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
//                if (error == null && tokenInfo != null) {
//                    getKakaoInfo(resultListener)
//                }
//            }
//        }
//    }
//
//    private fun getKakaoInfo(resultListener: SNSLoginResultListener) {
//        UserApiClient.instance.me { user, error ->
//            if (error != null) {
//                resultListener.onFail("KAKAO > me > onFailure: ${error.message}")
//            } else {
//                val snsLoginInfo = SNSUserInfo(
//                    socialId = user?.id.toString(),
//                    snsType = SNSType.KAKAO,
//                    email = user?.kakaoAccount?.email,
//                    name = user?.kakaoAccount?.legalName,
//                    nickname = user?.kakaoAccount?.profile?.nickname
//                )
//                resultListener.onSuccess(snsLoginInfo)
//            }
//        }
//    }
//}