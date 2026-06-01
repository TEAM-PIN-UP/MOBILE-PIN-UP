package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.platform.UseLightStatusBarIcons
import com.pinup.placePinup.ui.model.TitleBarButtonType
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import com.preat.peekaboo.ui.camera.PeekabooCamera
import com.preat.peekaboo.ui.camera.rememberPeekabooCameraState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.camera_permission_denied_description
import pinup.composeapp.generated.resources.camera_permission_denied_title
import pinup.composeapp.generated.resources.ic_back
import pinup.composeapp.generated.resources.ic_close
import pinup.composeapp.generated.resources.ic_rotate

@Composable
fun CameraView(
    onCapture: (ByteArray) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    UseLightStatusBarIcons()

    val state = rememberPeekabooCameraState(
        onCapture = { bytes -> bytes?.let(onCapture) },
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.Black)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        PeekabooCamera(
            state = state,
            modifier = Modifier.fillMaxSize().padding(top = 56.dp),
            permissionDeniedContent = {
                CameraPermissionDenied(onDismiss = onDismiss)
            },
        )

        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            CameraTopBar(
                onClose = onDismiss,
                modifier = Modifier.align(Alignment.TopStart),
            )

            CameraBottomBar(
                isCapturing = state.isCapturing,
                onCapture = { if (!state.isCapturing) state.capture() },
                onToggleCamera = { state.toggleCamera() },
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun CameraTopBar(
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TitleBar(
        buttonType = TitleBarButtonType.CLOSE,
        onLeftButtonClick = onClose
    )
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_back),
            contentDescription = "close camera",
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .clickableWithNoRipple { onClose() },
        )
    }
}

@Composable
private fun CameraBottomBar(
    isCapturing: Boolean,
    onCapture: () -> Unit,
    onToggleCamera: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 32.dp),
    ) {
        ShutterButton(
            isCapturing = isCapturing,
            onClick = onCapture,
            modifier = Modifier.align(Alignment.Center),
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Colors.White)
                .clickableWithNoRipple { onToggleCamera() }
                .align(Alignment.CenterEnd),
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_rotate),
                contentDescription = "toggle camera",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
            )
        }
    }
}

@Composable
private fun ShutterButton(
    isCapturing: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .border(width = 4.dp, color = Colors.White, shape = CircleShape)
            .padding(6.dp)
            .clip(CircleShape)
            .background(if (isCapturing) Colors.Gray400 else Colors.White)
            .clickableWithNoRipple(enabled = !isCapturing) { onClick() },
    )
}

@Composable
private fun CameraPermissionDenied(
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Black)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_close),
            contentDescription = "close",
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
                .size(24.dp)
                .clickableWithNoRipple { onDismiss() },
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(Res.string.camera_permission_denied_title),
                style = Typography.T1.copy(fontWeight = FontWeight.SemiBold),
                color = Colors.White,
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(Res.string.camera_permission_denied_description),
                style = Typography.B3,
                color = Colors.Gray300,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
