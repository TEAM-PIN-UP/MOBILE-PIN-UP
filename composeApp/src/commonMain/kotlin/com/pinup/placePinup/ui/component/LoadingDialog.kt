package com.pinup.placePinup.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pinup.placePinup.ui.theme.Colors

/**
 * 서버 통신 중 화면 조작을 막는 로딩 다이얼로그.
 * 요청이 끝나기 전에 뒤로가기·바깥 터치로 닫혀 중복 요청이 나가지 않도록 스스로는 닫히지 않는다.
 */
@Composable
fun LoadingDialog() {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Colors.Main
            )
        }
    }
}
