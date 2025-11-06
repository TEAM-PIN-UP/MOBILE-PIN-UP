package com.pinup.placePinup.ui.component

import androidx.compose.runtime.Composable
import com.pinup.placePinup.AlertState


@Composable
fun LogoutDialog(
    alertState: AlertState,
    onLogoutClick: () -> Unit,
    onDisMissRequest: () -> Unit,
) {
    if (alertState.isShow) {
        PDialog(
            titleText = alertState.title,
            rightButtonText = "확인",
            onRightButtonClick = {
                onLogoutClick()
                onDisMissRequest()
            }
        )
    }
}