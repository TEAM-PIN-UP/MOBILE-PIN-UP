package com.pinup.pinup.ui.findAccount.findPassword

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.component.ErrorText
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.signup.EmailVerifyType
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography

@Composable
fun FindPasswordEmailScreen(
    onMovePassword: () -> Unit,
    emailState: ChangePasswordEmailState = ChangePasswordEmailState(),
    onEmailChanged: (String) -> Unit = {},
    onCodeChanged: (String) -> Unit = {},
    onClickSendCode: () -> Unit = {},
    onClickVerify: () -> Unit = {},
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

        Spacer(modifier = Modifier.height(44.dp))

        Text(
            text = Texts.FindPassword.TITLE,
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Gray800
        )

        Spacer(modifier = Modifier.height(69.dp))

        Text(
            text = Texts.Word.EMAIL,
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray700
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box{
            RoundedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                text = emailState.email,
                onValueChange = onEmailChanged,
                placeholder = Texts.SignupEmail.HINT,
                cornerRounded = 100,
                backgroundColor = Colors.White,
                isError = !emailState.isEmailValid,
            )

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
                            if(emailState.isEmailValid && emailState.email.isNotEmpty()) onClickSendCode()
                        },
                    cornerRounded = 100,
                    backgroundColor = if(emailState.isEmailValid && emailState.email.isNotEmpty() && !emailState.isClicked) Colors.Gray800 else Colors.Gray300,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .align(Alignment.Center),
                        text = Texts.Word.VERIFY,
                        style = Typography.L3.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.White
                    )
                }

                Spacer(modifier = Modifier.width(19.dp))
            }
        }

        if (!emailState.isEmailValid) {
            Spacer(modifier = Modifier.height(8.dp))
            ErrorText(Texts.SignupEmail.INVALID)
        } else if (emailState.isClicked) {
            Spacer(modifier = Modifier.height(8.dp))
            ErrorText(
                text = Texts.SignupEmail.SEND_CODE,
                isNotError = true
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box{
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
                            if(emailState.verificationCode.isNotEmpty()) onClickVerify()
                        },
                    cornerRounded = 100,
                    backgroundColor = if(emailState.verificationCode.isNotEmpty()) Colors.Gray800 else Colors.Gray300,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 6.dp)
                            .align(Alignment.Center),
                        text = Texts.Word.CONFIRM,
                        style = Typography.L3.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.White
                    )
                }

                Spacer(modifier = Modifier.width(19.dp))
            }
        }

        if(emailState.emailVerifyType == EmailVerifyType.NOT_VERIFIED) {
            Spacer(modifier = Modifier.height(8.dp))
            ErrorText(Texts.SignupEmail.CODE_INVALID)
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            modifier = Modifier
                .padding(bottom = 28.dp),
            text = Texts.FindPassword.CHANGE_PASSWORD,
            onClick = {
                onMovePassword()
            },
            isEnable = emailState.isPassValidation
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}