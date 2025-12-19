package com.pinup.placePinup

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.pinup.placePinup.di.initKoin
import com.pinup.placePinup.platform.GoogleLoginController
import com.pinup.placePinup.ui.login.sns.AppleLoginController
import com.pinup.placePinup.ui.login.sns.KaKaoLoginController
import com.pinup.placePinup.ui.login.sns.KaKaoShareController
import com.pinup.placePinup.ui.login.sns.NaverLoginController
import com.pinup.placePinup.ui.login.sns.SNSLoginFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.dsl.module

class PinUpApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
        NaverIdLoginSDK.initialize(
            this,
            BuildConfig.NAVER_CLIENT_ID,
            BuildConfig.NAVER_CLIENT_SECRET,
            getString(R.string.app_name)
        )
        Firebase.initialize(this)
        initKoin(
            module = module {
                single<KaKaoLoginController> { AndroidKaKaoLoginController() }
                single<KaKaoShareController> { AndroidKaKaoShareController() }
                single<NaverLoginController> { AndroidNaverLoginController() }
                single<AppleLoginController> { AndroidAppleLoginController() }
                single<GoogleLoginController> { GoogleLoginController() }
                single<SNSLoginFactory> { SNSLoginFactory(get(), get(), get(), get()) }
            },
            appDeclaration = {
                androidLogger()
                androidContext(this@PinUpApplication)
            }
        )
    }
}