package com.pinup.placePinup.ui.findAccount.findPassword
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

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
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.ui.component.ErrorText
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.RoundedTextField
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography

@Composable
fun FindPasswordEmailScreen(
    email: String = "",
    isEmailValid: Boolean = false,
    onClickSendPassword: () -> Unit,
    onEmailChanged: (String) -> Unit = {},
    onBackPressed: () -> Unit,
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

        Spacer(modifier = Modifier.height(49.dp))

        Text(
            text = Texts.FindPassword.FIND_PASSWORD,
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Gray800
        )

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = Texts.FindPassword.FIND_PASSWORD,
            style = Typography.B2.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray400
        )

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            text = stringResource(Res.string.word_email),
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray700
        )

        Spacer(modifier = Modifier.height(3.dp))

        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            text = email,
            onValueChange = onEmailChanged,
            placeholder = Texts.SignupEmail.HINT,
            cornerRounded = 100,
            backgroundColor = Colors.White,
            isError = isEmailValid,
        )

        if (isEmailValid) {
            Spacer(modifier = Modifier.height(8.dp))
            ErrorText(Texts.SignupEmail.INVALID)
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            modifier = Modifier
                .padding(bottom = 28.dp),
            text = Texts.FindPassword.SEND_PASSWORD,
            onClick = {
                onClickSendPassword()
            },
            isEnable = isEmailValid
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}