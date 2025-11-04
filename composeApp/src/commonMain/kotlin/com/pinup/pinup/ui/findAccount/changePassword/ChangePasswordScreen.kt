package com.pinup.pinup.ui.findAccount.changePassword

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
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.component.ErrorText
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_password_show_enable
import pinup.composeapp.generated.resources.ic_password_show_unable

@Composable
fun ChangePasswordScreen(
    password: String = "",
    passwordAgain: String = "",
    isPasswordValid: Boolean = true,
    isShowPassword: Boolean = false,
    isPasswordMatched: Boolean = true,
    onPasswordChanged: (String) -> Unit,
    onPasswordAgainChanged: (String) -> Unit,
    onClickShowPassword : () -> Unit,
    onClickConfirm: () -> Unit,
    onBackPressed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp)
    ) {
        TitleBar(
            onLeftButtonClick = onBackPressed
        )

        Spacer(modifier = Modifier.height(44.dp))

        Text(
            text = Texts.FindPassword.CHANGE_PASSWORD,
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Black
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = Texts.FindPassword.CHANGE_PASSWORD_HINT,
            style = Typography.B2.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray400
        )

        Spacer(modifier = Modifier.height(38.dp))

        Text(
            text = Texts.Word.WORD_PASSWORD,
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Neutral700
        )

        Spacer(modifier = Modifier.height(3.dp))

        RoundedTextField(
            modifier = Modifier,
            text = password,
            onValueChange = {
                onPasswordChanged(it)
            },
            cornerRounded = 100,
            placeholder = Texts.FindPassword.PASSWORD_HINT,
            textStyle = Typography.B3,
            isError = !isPasswordValid,
        )

        if(!isPasswordValid){
            Spacer(modifier = Modifier.height(8.dp))

            ErrorText(Texts.SignupPassword.INVALID)
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = Texts.FindPassword.PASSWORD_AGAIN_HINT,
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Neutral700
        )

        Spacer(modifier = Modifier.height(3.dp))

        Box{
            RoundedTextField(
                modifier = Modifier,
                text = passwordAgain,
                onValueChange = {
                    onPasswordAgainChanged(it)
                },
                visualTransformation = if(isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                cornerRounded = 100,
                isError = !isPasswordMatched
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
                    painter = painterResource(if(isShowPassword) Res.drawable.ic_password_show_enable else Res.drawable.ic_password_show_unable),
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(17.dp))
            }

        }

        if(!isPasswordMatched){
            Spacer(modifier = Modifier.height(8.dp))

            ErrorText(Texts.SignupPassword.NOT_MATCH)
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            text = Texts.FindPassword.CHANGE_PASSWORD,
            isEnable = isPasswordValid,
            onClick = {
                onClickConfirm()
            }
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}