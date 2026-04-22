package com.pinup.placePinup.ui.component
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography

@Composable
fun FindIdBottomSheet(
    profileUrl: String = "",
    nickname: String = "",
    onClickFindPassword: () -> Unit = {},
    onClickLogin: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(28.dp))

        Text(
            textAlign = TextAlign.Center,
            text = stringResource(Res.string.find_id_result),
            style = Typography.T2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Gray800
        )

        Spacer(Modifier.height(32.dp))

        ProfileImageView(
            imgUrl = profileUrl,
            size = 60.dp
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = nickname,
            style = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray800
        )

        Spacer(Modifier.height(36.dp))

        PButton(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            text = stringResource(Res.string.find_password_change_password),
            onClick = {
                onClickFindPassword()
            }
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .clickableWithNoRipple {
                    onClickLogin()
                },
            text = stringResource(Res.string.find_password_do_login),
            style = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray600
        )

        Spacer(Modifier.height(42.dp))

    }
}