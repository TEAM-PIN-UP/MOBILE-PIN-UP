package com.pinup.placePinup.ui.signup.compose
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.ui.component.ErrorText
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.RoundedTextField
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.signup.PasswordState
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_password_show_enable
import pinup.composeapp.generated.resources.ic_password_show_unable

@Composable
fun InputPasswordScreen(
    passwordState: PasswordState,
    onPasswordChanged: (String) -> Unit,
    onPasswordAgainChanged: (String) -> Unit,
    onClickShowFirstPassword : () -> Unit,
    onClickShowPassword : () -> Unit,
    onClickConfirm: () -> Unit,
    onBackPressed : () -> Unit ={},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp)
    ) {
        TitleBar(
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        Spacer(modifier = Modifier.height(49.dp))
        Text(
            text = stringResource(Res.string.signup_password_title),
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Black
        )

        Spacer(modifier = Modifier.height(31.dp))

        Text(
            text = stringResource(Res.string.word_password),
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray700
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box{
            RoundedTextField(
                modifier = Modifier,
                text = passwordState.password,
                textStyle = Typography.B2.copy(
                    fontWeight = FontWeight.Medium
                ),
                onValueChange = {
                    onPasswordChanged(it)
                },
                cornerRounded = 100,
                visualTransformation = if(passwordState.isShowFirstPassword) VisualTransformation.None else PasswordVisualTransformation(),
                placeholder = stringResource(Res.string.signup_password_hint),
                isError = !passwordState.isPasswordValid,
            )

            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.clickableSingleWithNoRipple {
                        onClickShowFirstPassword()
                    },
                    painter = painterResource(if(passwordState.isShowFirstPassword) Res.drawable.ic_password_show_enable else Res.drawable.ic_password_show_unable),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(17.dp))
            }
        }

        if(!passwordState.isPasswordValid){
            Spacer(modifier = Modifier.height(8.dp))

            ErrorText(stringResource(Res.string.signup_password_invalid))

            Spacer(modifier = Modifier.height(14.dp))
        }
        else{
            Spacer(modifier = Modifier.height(41.dp))
        }

        Text(
            text = stringResource(Res.string.signup_password_confirm),
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray700
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box{
            RoundedTextField(
                modifier = Modifier,
                text = passwordState.passwordAgain,
                textStyle = Typography.B2.copy(
                    fontWeight = FontWeight.Medium
                ),
                onValueChange = {
                    onPasswordAgainChanged(it)
                },
                visualTransformation = if(passwordState.isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                cornerRounded = 100,
                isError = !passwordState.isPasswordMatched,
            )

            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.clickableSingleWithNoRipple {
                        onClickShowPassword()
                    },
                    painter = painterResource(if(passwordState.isShowPassword) Res.drawable.ic_password_show_enable else Res.drawable.ic_password_show_unable),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(17.dp))
            }
        }

        if(!passwordState.isPasswordMatched){
            Spacer(modifier = Modifier.height(8.dp))

            ErrorText(stringResource(Res.string.signup_password_not_match))
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            text = stringResource(Res.string.word_confirm),
            isEnable = passwordState.isPassValidation,
            onClick = {
                onClickConfirm()
            }
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}