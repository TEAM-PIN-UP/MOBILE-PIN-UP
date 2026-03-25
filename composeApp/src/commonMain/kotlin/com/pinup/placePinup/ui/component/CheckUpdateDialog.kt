package com.pinup.placePinup.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import com.pinup.placePinup.ui.theme.Texts
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ForcedCheckUpdateDialog(
    title: String = Texts.Update.CHECK_UPDATE_TITLE,
    description: String = Texts.Update.CHECK_UPDATE_DESCRIPTION,
    onConfirm: () -> Unit,
) {
    PDialog(
        titleText = title,
        descriptionText = description,
        rightButtonText = Texts.Update.CHECK_UPDATE_CONFIRM,
        onRightButtonClick = onConfirm,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    )
}

@Composable
fun OptionalCheckUpdateDialog(
    title: String = Texts.Update.CHECK_UPDATE_TITLE,
    description: String = Texts.Update.CHECK_UPDATE_DESCRIPTION,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    PDialog(
        titleText = title,
        descriptionText = description,
        leftButtonText = Texts.Update.CHECK_UPDATE_OPTIONAL_CANCEL,
        rightButtonText = Texts.Update.CHECK_UPDATE_CONFIRM,
        onLeftButtonClick = onDismiss,
        onRightButtonClick = onConfirm,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    )
}

@Preview
@Composable
fun ForcedCheckUpdateDialogPreview() {
    ForcedCheckUpdateDialog(
        onConfirm = {}
    )
}

@Preview
@Composable
fun OptionalCheckUpdateDialogPreview() {
    OptionalCheckUpdateDialog(
        onConfirm = {},
        onDismiss = {}
    )
}
