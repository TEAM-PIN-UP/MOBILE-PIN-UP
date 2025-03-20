package com.pinup.pinup

import coil3.PlatformContext
import coil3.toUri
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun openBrowser(url: String, context: PlatformContext) {
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null) {
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}

actual fun getRealPathFromUri(contentUri: String, context: PlatformContext): String? {
    return contentUri
}

actual fun hLog(message: String) {
    return NSLog(message)
}