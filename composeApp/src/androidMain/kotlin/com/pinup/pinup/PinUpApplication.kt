package com.pinup.pinup

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk
import com.navercorp.nid.NaverIdLoginSDK
import com.pinup.pinup.di.initKoin
import com.pinup.pinup.domain.usecase.CheckNickNameUseCase
import com.pinup.pinup.platform.GoogleLoginController
import com.pinup.pinup.ui.login.sns.KaKaoLoginController
import com.pinup.pinup.ui.login.sns.KaKaoShareController
import com.pinup.pinup.ui.login.sns.NaverLoginController
import com.pinup.pinup.ui.login.sns.SNSLoginFactory
import org.koin.android.ext.koin.androidApplication
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
        initKoin(
            module = module {
                single<KaKaoLoginController> { AndroidKaKaoLoginController() }
                single<KaKaoShareController> { AndroidKaKaoShareController() }
                single<NaverLoginController> { AndroidNaverLoginController() }
                single<GoogleLoginController> { GoogleLoginController() }
                single<SNSLoginFactory> { SNSLoginFactory(get(), get(), get()) }
            },
            appDeclaration = {
                androidLogger()
                androidContext(this@PinUpApplication)
            }
        )
    }
}