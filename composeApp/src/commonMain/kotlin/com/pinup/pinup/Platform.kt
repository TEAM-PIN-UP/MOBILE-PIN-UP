package com.pinup.pinup

import coil3.PlatformContext

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun openBrowser(url: String, context: PlatformContext)
expect fun getRealPathFromUri(contentUri: String, context: PlatformContext): String?
expect fun hLog(message: String)