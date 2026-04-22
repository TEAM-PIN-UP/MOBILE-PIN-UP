package com.pinup.placePinup.ui.component.bottomSheet
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_delete
import pinup.composeapp.generated.resources.ic_gallery

@Composable
fun ProfileMenuBottomSheet(
    onClickSelectImage: () -> Unit = {},
    onClickDelete: () -> Unit = {},
) {
    Spacer(Modifier.height(40.dp))
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple {
                    onClickSelectImage()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                contentScale = ContentScale.Crop,
                painter = painterResource(Res.drawable.ic_gallery),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(Res.string.profile_pick_library),
                color = Colors.Gray800,
                style = Typography.T2.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple {
                    onClickDelete()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color = Colors.Negative),
                painter = painterResource(Res.drawable.ic_delete),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(Res.string.profile_delete_profile_image),
                color = Colors.Negative,
                style = Typography.T2.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }

    Spacer(Modifier.height(64.dp))
}
