package com.pinup.pinup.ui.profilesetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors

@Composable
fun ProfileSettingScreen(
    onBackPressed: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        TitleBar(
            title = "프로필 편집",
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        PHorizontalDivider()

    }
}