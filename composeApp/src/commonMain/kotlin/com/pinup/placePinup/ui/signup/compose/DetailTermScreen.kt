package com.pinup.placePinup.ui.signup.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors

@Composable
fun DetailTermScreen(
    onBackPressed: () -> Unit,
    url : String = "",
) {
    val state = rememberWebViewState(url)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp),
    ) {
        TitleBar(
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        WebView(state)
    }
}