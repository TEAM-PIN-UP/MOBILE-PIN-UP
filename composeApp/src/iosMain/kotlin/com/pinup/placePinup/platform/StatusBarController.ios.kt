package com.pinup.placePinup.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIStatusBarStyle
import platform.UIKit.UIStatusBarStyleDarkContent
import platform.UIKit.UIStatusBarStyleLightContent
import platform.UIKit.setStatusBarStyle

@Composable
actual fun UseLightStatusBarIcons() {
    DisposableEffect(Unit) {
        val app = UIApplication.sharedApplication
        val previous: UIStatusBarStyle = app.statusBarStyle
        app.setStatusBarStyle(UIStatusBarStyleLightContent, animated = true)
        onDispose {
            val restore = if (previous == UIStatusBarStyleLightContent) {
                UIStatusBarStyleDarkContent
            } else {
                previous
            }
            app.setStatusBarStyle(restore, animated = true)
        }
    }
}
