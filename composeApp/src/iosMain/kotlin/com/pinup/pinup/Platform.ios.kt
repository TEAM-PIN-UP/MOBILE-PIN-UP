package com.pinup.pinup

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import coil3.PlatformContext
import coil3.toUri
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.bytestring.toByteString
import platform.Foundation.NSFileManager
import platform.Foundation.NSLibraryDirectory
import platform.Foundation.NSLog
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
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
actual class PlatformFile actual constructor(private val uri: String) {
    actual val name: String = uri.split("/").last()

    actual suspend fun toByteArray(): ByteArray {
        val file = NSFileManager.defaultManager.contentsAtPath(uri)
        return file?.toByteString()?.toByteArray() ?: ByteArray(0)
    }
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
