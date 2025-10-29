package com.pinup.pinup.platform

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun PlatformWebView(modifier: Modifier, url: String, html: String) {
    val context = LocalContext.current
    AndroidView(
        factory = { WebView(context).apply { webViewClient = WebViewClient() } },
        modifier = Modifier.then(modifier),
        update = {
            if(url.isEmpty()) {
                it.loadData(html, "text/html; charset=utf-8", "UTF-8")
            }
            else {
                it.loadUrl(url)
            }
        }
    )
}