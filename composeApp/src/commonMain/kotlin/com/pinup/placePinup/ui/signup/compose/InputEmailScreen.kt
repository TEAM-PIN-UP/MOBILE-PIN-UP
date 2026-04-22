package com.pinup.placePinup.ui.signup.compose
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

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
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.ui.component.ErrorText
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.component.RoundedTextField
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.signup.EmailState
import com.pinup.placePinup.ui.signup.EmailVerifyType
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography

@Composable
fun InputEmailScreen(
    onMovePassword: () -> Unit,
    emailState: EmailState = EmailState(),
    onEmailChanged : (String) -> Unit = {},
    onCodeChanged : (String) -> Unit = {},
    onClickVerify : () -> Unit = {},
    onBackPressed : () -> Unit ={},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp),
    ) {
        TitleBar(
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        Spacer(modifier = Modifier.height(49.dp))

        Text(
            text = Texts.SignupEmail.TITLE,
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Gray800
        )

        Spacer(modifier = Modifier.height(31.dp))

        Text(
            text = stringResource(Res.string.word_email),
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray700
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
                        backgroundColor = Colors.Gray800,
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .align(Alignment.Center),
                            text = stringResource(Res.string.word_verify),
                            style = Typography.L3.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
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
            Box {
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
                        .padding(vertical = 17.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = emailState.timer,
                        style = Typography.L2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Error
                    )

                    Spacer(modifier = Modifier.width(17.dp))
                }

            }

            if (emailState.emailVerifyType == EmailVerifyType.NOT_VERIFIED) {
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(Texts.SignupEmail.CODE_INVALID)
            } else if (emailState.emailVerifyType == EmailVerifyType.VERIFIED){
                Spacer(modifier = Modifier.height(8.dp))
                ErrorText(Texts.SignupEmail.CODE_VALID, isNotError = true)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            modifier = Modifier
                .padding(bottom = 28.dp),
            text = stringResource(Res.string.word_confirm),
            onClick = {
                onMovePassword()
            },
            isEnable = emailState.isPassValidation
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}