package com.pinup.pinup

import com.pinup.pinup.di.initKoin
import com.pinup.pinup.ui.login.sns.KaKaoLoginController
import org.koin.dsl.module

fun init(
    kaKaoLoginController: KaKaoLoginController
) {
    initKoin(
        module = module {
            single<KaKaoLoginController> { kaKaoLoginController }
        }
    )
}