package com.pinup.pinup

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.NativeViewFactory

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("LocalNativeViewFactory not provided")
}

fun MainViewController(
    nativeViewFactory: NativeViewFactory
) = ComposeUIViewController {
    val contextFactory = ContextFactory()
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        PinUpApp(
            contextFactory = contextFactory
        )
    }
}