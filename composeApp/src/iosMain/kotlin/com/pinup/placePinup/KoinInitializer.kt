package com.pinup.placePinup

import com.pinup.placePinup.di.initKoin
import com.pinup.placePinup.platform.GoogleLoginController
import com.pinup.placePinup.ui.login.sns.AppleLoginController
import com.pinup.placePinup.ui.login.sns.KaKaoLoginController
import com.pinup.placePinup.ui.login.sns.KaKaoShareController
import com.pinup.placePinup.ui.login.sns.NaverLoginController
import com.pinup.placePinup.ui.login.sns.SNSLoginFactory
import org.koin.dsl.module

fun init(
    kaKaoLoginController: KaKaoLoginController,
    kaKaoShareController: KaKaoShareController,
    naverLoginController: NaverLoginController,
    appleLoginController: AppleLoginController,
) {
    initKoin(
        module = module {
            single<KaKaoLoginController> { kaKaoLoginController }
            single<KaKaoShareController> { kaKaoShareController }
            single<NaverLoginController> { naverLoginController }
            single<AppleLoginController> { appleLoginController }
            single<GoogleLoginController> { GoogleLoginController() }
            single<SNSLoginFactory> { SNSLoginFactory(get(), get(), get(), get()) }
        }
    )
}