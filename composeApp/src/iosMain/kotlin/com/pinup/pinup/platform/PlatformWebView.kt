package com.pinup.pinup.platform

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSMutableURLRequest
import platform.Foundation.NSNumber
import platform.Foundation.NSURL
import platform.WebKit.*

private object SharedWebKitObjects {
    val processPool = WKProcessPool()
}

@OptIn(ExperimentalForeignApi::class, ExperimentalComposeUiApi::class)
@Composable
actual fun PlatformWebView(
    modifier: Modifier,
    url: String,
    html: String
) {
    val density = LocalDensity.current
    var contentHeight by remember { mutableStateOf(1.dp) } // 웹뷰 실제 높이
    // 네비게이션 완료 시점에 콘텐츠 높이 측정
    val navDelegate = remember {
        object : platform.darwin.NSObject(), WKNavigationDelegateProtocol {
            override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
                // JS로 실제 렌더링된 DOM 높이를 계산
                webView.evaluateJavaScript(
                    """
                Math.max(
                    document.body.scrollHeight,
                    document.documentElement.scrollHeight,
                    document.body.offsetHeight,
                    document.documentElement.offsetHeight
                )
                """
                ) { result, _ ->
                    val h = (result as? NSNumber)?.doubleValue ?: 0.0
                    if (h > 0.0) {
                        contentHeight = h.dp
                        hLog("하이" + contentHeight.toString())
                    }
                }
            }
        }
    }

    UIKitView(
        factory = {
            val controller = WKUserContentController().apply {
                // viewport 메타가 없을 때 자동 삽입 (글자 작게 보이는 문제 방지)
                addUserScript(
                    WKUserScript(
                        source = """
                            if (!document.querySelector('meta[name=viewport]')) {
                              var m = document.createElement('meta');
                              m.name = 'viewport';
                              m.content = 'width=device-width, initial-scale=1.0';
                              document.head.appendChild(m);
                            }
                        """.trimIndent(),
                        injectionTime = WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentEnd,
                        forMainFrameOnly = true
                    )
                )
            }

            val config = WKWebViewConfiguration().apply {
                processPool = SharedWebKitObjects.processPool
                userContentController = controller
                defaultWebpagePreferences = WKWebpagePreferences().apply {
                    preferredContentMode = WKContentMode.WKContentModeMobile
                }
            }

            WKWebView(
                frame = CGRectMake(0.0, 0.0, 0.0, 0.0),
                configuration = config
            ).apply {
                allowsBackForwardNavigationGestures = true
                navigationDelegate = navDelegate
                scrollView.scrollEnabled = false
            }
        },
        update = { webView ->
            when {
                html.isNotBlank() -> {
                    val htmlWithViewport =
                        if (html.contains("name=\"viewport\"") || html.contains("name='viewport'")) html
                        else """
                            <head>
                              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                            </head>
                            $html
                        """.trimIndent()
                    webView.loadHTMLString(htmlWithViewport, baseURL = null)
                }
                url.isNotBlank() -> {
                    val request = NSMutableURLRequest.requestWithURL(NSURL(string = url))
                    webView.loadRequest(request)
                }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 1000.dp)
            .height(contentHeight),
        onRelease = { view ->
            (view as? WKWebView)?.navigationDelegate = null
        },
    )
}
