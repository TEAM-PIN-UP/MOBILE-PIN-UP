package com.pinup.pinup

import androidx.compose.ui.window.ComposeUIViewController
import com.pinup.pinup.platform.ContextFactory

fun MainViewController() = ComposeUIViewController {
    val contextFactory = ContextFactory()
    PinUpApp(
        contextFactory = contextFactory
    )
}