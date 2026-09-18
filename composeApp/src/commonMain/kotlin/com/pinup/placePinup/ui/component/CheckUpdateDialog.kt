package com.pinup.placePinup.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*

@Composable
fun ForcedCheckUpdateDialog(
    title: String,
    description: String,
    onConfirm: () -> Unit,
) {
    PDialog(
        titleText = title.ifEmpty { stringResource(Res.string.update_check_title) },
        descriptionText = description.ifEmpty { stringResource(Res.string.update_check_description) },
        rightButtonText = stringResource(Res.string.update_check_confirm),
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
    title: String,
    description: String,
    onConfirm: () -> Unit,
    onRemindLater: () -> Unit,
    onDismiss: () -> Unit,
) {
    PDialog(
        titleText = title.ifEmpty { stringResource(Res.string.update_check_title) },
        descriptionText = description.ifEmpty { stringResource(Res.string.update_check_description) },
        leftButtonText = stringResource(Res.string.update_check_optional_cancel),
        rightButtonText = stringResource(Res.string.update_check_confirm),
        onDismissRequest = onDismiss,
        onLeftButtonClick = onRemindLater,
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
        title = "",
        description = "",
        onConfirm = {}
    )
}

@Preview
@Composable
fun OptionalCheckUpdateDialogPreview() {
    OptionalCheckUpdateDialog(
        title = "",
        description = "",
        onConfirm = {},
        onDismiss = {},
        onRemindLater = {}
    )
}
