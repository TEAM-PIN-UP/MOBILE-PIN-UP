package com.pinup.pinup.ui.signup.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.component.ErrorText
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.signup.EmailState
import com.pinup.pinup.ui.signup.EmailVerifyType
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography

@Composable
fun InputEmailScreen(
    onMovePassword: () -> Unit,
    emailState: EmailState = EmailState(),
    onEmailChanged : (String) -> Unit = {},
    onCodeChanged : (String) -> Unit = {},
    onClickVerify : () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(49.dp))

        Text(
            text = Texts.SignupEmail.TITLE,
            style = Typography.H0,
            color = Colors.Black
        )

        Spacer(modifier = Modifier.height(31.dp))

        Text(
            text = Texts.Word.EMAIL,
            style = Typography.B2,
            color = Colors.Neutral700
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            RoundedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                text = emailState.email,
                onValueChange = onEmailChanged,
                placeholder = Texts.SignupEmail.HINT,
                cornerRounded = 100,
                backgroundColor = Colors.White,
                isError = !emailState.isEmailValid || emailState.isEmailUsed,
            )

            if (emailState.isEmailValid && !emailState.isEmailUsed && emailState.email.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(top = 11.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundedBox(
                        modifier = Modifier
                            .clickableSingleWithNoRipple {
                                onClickVerify()
                            },
                        cornerRounded = 100,
                        backgroundColor = Colors.Neutral800,
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .align(Alignment.Center),
                            text = Texts.Word.VERIFY,
                            style = Typography.B7,
                            color = Colors.White
                        )
                    }

                    Spacer(modifier = Modifier.width(17.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!emailState.isEmailValid) {
            ErrorText(Texts.SignupEmail.INVALID)
        } else if (emailState.isEmailUsed) {
            ErrorText(Texts.SignupEmail.DUPLICATE)
        }

        if (emailState.isClickedVerify) {
            RoundedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                text = emailState.verificationCode,
                onValueChange = onCodeChanged,
                placeholder = Texts.SignupEmail.CODE_HINT,
                cornerRounded = 100,
                backgroundColor = Colors.White,
                isError = emailState.emailVerifyType == EmailVerifyType.NOT_VERIFIED,
            )

            if (emailState.emailVerifyType == EmailVerifyType.NOT_VERIFIED) {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(Texts.SignupEmail.CODE_INVALID)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            modifier = Modifier
                .padding(bottom = 28.dp),
            text = Texts.Word.CONFIRM,
            onClick = {
                onMovePassword()
            },
            isEnable = emailState.isPassValidation
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}