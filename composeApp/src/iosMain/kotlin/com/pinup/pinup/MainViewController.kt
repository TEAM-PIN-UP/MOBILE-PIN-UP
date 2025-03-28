package com.pinup.pinup

import androidx.compose.ui.window.ComposeUIViewController
import com.pinup.pinup.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    PinUpApp()
}