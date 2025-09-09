package com.pinup.pinup.ui.findAccount.findId

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography

@Composable
fun SuccessFindIdScreen(
    email: String = "",
    onBackPressed: () -> Unit,
    onClickLogin: () -> Unit,
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
            text = Texts.FindPassword.SENT_PASSWORD_TITLE,
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Gray800
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = Texts.FindPassword.SENT_PASSWORD_HINT,
            style = Typography.B2.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray400
        )

        Spacer(modifier = Modifier.height(161.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = email,
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Main,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            modifier = Modifier
                .padding(bottom = 28.dp),
            text = Texts.FindPassword.DO_LOGIN,
            onClick = {
                onClickLogin()
            },
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}