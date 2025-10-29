package com.pinup.pinup.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.WebKit.WKProcessPool
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration

private object SharedWebKitObjects {
    val processPool = WKProcessPool()
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformWebView(
    modifier: Modifier,
    url: String,
    html: String
) {
    UIKitView(
        factory = {
            val config = WKWebViewConfiguration().apply {
                processPool = SharedWebKitObjects.processPool
            }
            WKWebView(
                frame = CGRectMake(0.0, 0.0, 0.0, 0.0),
                configuration = config
            ).apply {
                allowsBackForwardNavigationGestures = true
            }
        },
        update = { webView ->
            when {
                html.isNotBlank() -> {
                    webView.loadHTMLString(html, baseURL = null)
                }
                url.isNotBlank() -> {
                    val request = NSMutableURLRequest.requestWithURL(URL = NSURL(string = url))
                    webView.loadRequest(request)
                }
                else -> {
                }
            }
        },
        modifier = modifier
    )
}
