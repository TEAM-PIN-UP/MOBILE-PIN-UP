package com.pinup.pinup

import com.pinup.pinup.di.initKoin
import com.pinup.pinup.platform.GoogleLoginController
import com.pinup.pinup.ui.login.sns.KaKaoLoginController
import com.pinup.pinup.ui.login.sns.KaKaoShareController
import com.pinup.pinup.ui.login.sns.NaverLoginController
import com.pinup.pinup.ui.login.sns.SNSLoginFactory
import org.koin.dsl.module

fun init(
    kaKaoLoginController: KaKaoLoginController,
    kaKaoShareController: KaKaoShareController,
    naverLoginController: NaverLoginController,
) {
    initKoin(
        module = module {
            single<KaKaoLoginController> { kaKaoLoginController }
            single<KaKaoShareController> { kaKaoShareController }
            single<NaverLoginController> { naverLoginController }
            single<GoogleLoginController> { GoogleLoginController() }
            single<SNSLoginFactory> { SNSLoginFactory(get(), get(), get()) }
        }
    )
}