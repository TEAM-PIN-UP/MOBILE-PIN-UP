package com.pinup.placePinup.ui.findAccount.findId

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.ProfileImageView
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import com.pinup.placePinup.util.Const

@Composable
fun SuccessFindIdScreen(
    profileUrl: String = "",
    nickName: String = "",
    email: String = "",
    onBackPressed: () -> Unit = {},
    onClickLogin: () -> Unit = {},
    onClickFindPassword: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp),
    ) {
        TitleBar(
            onLeftButtonClick = onBackPressed
        )

        Spacer(modifier = Modifier.height(44.dp))

        Text(
            text = Texts.FindId.getNickNameTitle(nickName),
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
        )

        Spacer(modifier = Modifier.height(81.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImageView(
                imgUrl = profileUrl,
                size = 100.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = nickName,
                style = Typography.B1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Black
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = Const.PRegex.maskEmail(email),
                style = Typography.D2.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Colors.Main
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            text = Texts.FindPassword.DO_LOGIN,
            onClick = {
                onClickLogin()
            }
        )

        Spacer(Modifier.height(16.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithNoRipple {
                    onClickFindPassword()
                },
            text = Texts.FindPassword.CHANGE_PASSWORD,
            style = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray600,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(42.dp))
    }
}