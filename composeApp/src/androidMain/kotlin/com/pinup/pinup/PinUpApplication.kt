package com.pinup.pinup

import android.app.Application
import com.pinup.pinup.di.initKoin
import com.pinup.pinup.ui.login.sns.KaKaoLoginController
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.dsl.module

class PinUpApplication : Application() {
    override fun onCreate() {
        super.onCreate()

//        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)
//        NaverIdLoginSDK.initialize(
//            this,
//            BuildConfig.NAVER_CLIENT_ID,
//            BuildConfig.NAVER_CLIENT_SECRET,
//            getString(R.string.app_name)
//        )
        initKoin(
            module = module {
                single<KaKaoLoginController> { AndroidKaKaoLoginController() }
            },
            appDeclaration = {
                androidLogger()
                androidContext(this@PinUpApplication)
            }
        )
    }
}