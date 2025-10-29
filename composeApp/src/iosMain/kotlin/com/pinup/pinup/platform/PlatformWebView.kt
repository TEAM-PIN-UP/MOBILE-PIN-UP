package com.pinup.pinup.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSURL
import platform.WebKit.WKWebView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PlatformWebView(
    modifier: Modifier,
    url: String,
    html: String
) {
    val webView = remember { WKWebView() }

    LaunchedEffect(url, html) {
        when {
            html.isNotBlank() -> {
                webView.loadHTMLString(html, baseURL = null)
            }
            url.isNotBlank() -> {
                val request = NSMutableURLRequest.requestWithURL(URL = NSURL(string = url))
                webView.loadRequest(request)
            }
        }
    }

    webView.allowsBackForwardNavigationGestures = true

    UIKitView(
        factory = { webView },
        modifier = modifier
    )
}
