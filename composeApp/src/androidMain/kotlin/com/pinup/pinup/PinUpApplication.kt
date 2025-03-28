package com.pinup.pinup

import android.app.Application
import com.pinup.pinup.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

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
        initKoin {
            androidLogger()
            androidContext(this@PinUpApplication)
        }
    }
}