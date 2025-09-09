package com.pinup.pinup.ui.component

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
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography

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
            text = Texts.FindId.FIND_ID_RESULT,
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
            text = Texts.FindPassword.CHANGE_PASSWORD,
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
            text = Texts.FindPassword.DO_LOGIN,
            style = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray600
        )

        Spacer(Modifier.height(42.dp))

    }
}