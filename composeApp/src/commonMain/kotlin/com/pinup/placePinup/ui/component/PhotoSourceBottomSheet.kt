package com.pinup.placePinup.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_camera
import pinup.composeapp.generated.resources.ic_camera_gray
import pinup.composeapp.generated.resources.ic_gallery
import pinup.composeapp.generated.resources.photo_source_camera
import pinup.composeapp.generated.resources.photo_source_gallery

@Composable
fun PhotoSourceBottomSheet(
    onClickGallery: () -> Unit = {},
    onClickCamera: () -> Unit = {},
) {
    Spacer(Modifier.height(40.dp))
    Column(
        modifier = Modifier.padding(horizontal = 16.dp),
    ) {
        PhotoSourceRow(
            label = stringResource(Res.string.photo_source_gallery),
            onClick = onClickGallery,
        )

        Spacer(modifier = Modifier.height(30.dp))

        PhotoSourceRow(
            label = stringResource(Res.string.photo_source_camera),
            onClick = onClickCamera,
        )
    }
    Spacer(Modifier.height(64.dp))
}

@Composable
private fun PhotoSourceRow(
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickableWithNoRipple { onClick() },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            color = Colors.Gray800,
            style = Typography.T2.copy(fontWeight = FontWeight.Medium),
        )
    }
}
