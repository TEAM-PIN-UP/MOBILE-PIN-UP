package com.pinup.placePinup.ui.component

import androidx.compose.runtime.Composable
import com.pinup.placePinup.AlertState
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.error_session_expired
import pinup.composeapp.generated.resources.word_confirm


@Composable
fun LogoutDialog(
    alertState: AlertState,
    onLogoutClick: () -> Unit,
    onDisMissRequest: () -> Unit,
) {
    if (alertState.isShow) {
        PDialog(
            titleText = alertState.title.ifEmpty { stringResource(Res.string.error_session_expired) },
            rightButtonText = stringResource(Res.string.word_confirm),
            onRightButtonClick = {
                onLogoutClick()
                onDisMissRequest()
            }
        )
    }
}