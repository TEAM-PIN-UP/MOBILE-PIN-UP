package com.pinup.pinup.platform

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import coil3.PlatformContext
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}


actual fun openBrowser(url: String, context: PlatformContext) {
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null) {
        UIApplication.sharedApplication.openURL(nsUrl)
    }
}

actual fun getRealPathFromUri(contentUri: String): String? {
    return contentUri
}

actual fun hLog(message: String) {
    return NSLog(message)
}
actual fun dataStorePreferences(): DataStore<Preferences> {
    return createDataStore(
        producePath = { producePath() }
    )
}

actual fun pxToDp(px: Float): Float {
    val scale = UIScreen.mainScreen.scale.toFloat()
    return px / scale
}

@OptIn(ExperimentalForeignApi::class)
private fun producePath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSLibraryDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory).path + "/$DATA_STORE_PREFERENCE"
}
