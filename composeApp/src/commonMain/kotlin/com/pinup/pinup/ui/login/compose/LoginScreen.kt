package com.pinup.pinup.ui.login.compose

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_google
import pinup.composeapp.generated.resources.ic_kakao
import pinup.composeapp.generated.resources.ic_naver
import pinup.composeapp.generated.resources.ic_search
import pinup.composeapp.generated.resources.ic_sns_login


@Composable
fun LoginScreen(
    onSnsLoginClick: (SNSType) -> Unit = {},
    id : String = "",
    password : String = "",
    isError: Boolean = false,
    onIdChanged : (String) -> Unit = {},
    onPasswordChanged : (String) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(223.dp))

        Text(
            modifier = Modifier,
            text = Texts.Onboarding.ONBOARDING_LOGIN_TITLE,
            style = Typography.H0
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier,
            text = Texts.Onboarding.ONBOARDING_LOGIN_TEXT,
            style = Typography.B2,
            color = Colors.Neutral400
        )

        Spacer(modifier = Modifier.height(49.dp))

        RoundedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            text = id,
            onValueChange = onIdChanged,
            placeholder = Texts.Word.WORD_ID,
            cornerRounded = 100,
            isError = isError
        )

        Spacer(modifier = Modifier.height(8.dp))

        RoundedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            text = password,
            onValueChange = onPasswordChanged,
            placeholder = Texts.Word.WORD_PASSWORD,
            cornerRounded = 100,
            visualTransformation = PasswordVisualTransformation(),
            isError = isError
        )

        Spacer(modifier = Modifier.height(20.dp))

        PButton(
            modifier = Modifier
                .padding(horizontal = 20.dp),
            text = Texts.Word.WORD_LOGIN,
            onClick = {
                onSnsLoginClick(SNSType.EMAIL)
            }
        )

        if (isError) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier,
                text = Texts.Login.INCORRECT_ID,
                style = Typography.B3,
                color = Colors.Error
            )

            Spacer(modifier = Modifier.height(10.dp))
        } else {
            Spacer(modifier = Modifier.height(15.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier,
                text = Texts.Login.LOGIN_ID_FIND,
                style = Typography.B4,
                color = Colors.Neutral700
            )

            Spacer(modifier = Modifier.width(27.dp))

            Text(
                modifier = Modifier,
                text = Texts.Login.LOGIN_PASSWORD_FIND,
                style = Typography.B4,
                color = Colors.Neutral700
            )
        }

        Spacer(modifier = Modifier.height(95.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .background(Colors.GrayLine)
            )

            Text(
                modifier = Modifier.padding(horizontal = 14.dp),
                text = Texts.Login.LOGIN_SNS,
                style = Typography.B4,
                color = Colors.Neutral400
            )

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .background(Colors.GrayLine)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 77.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(52.dp)
                    .clickableSingleWithNoRipple {
                        onSnsLoginClick(SNSType.KAKAO)
                    },
                painter = painterResource(Res.drawable.ic_kakao),
                contentDescription = "카카오 로그인"
            )

            Image(
                modifier = Modifier
                    .size(52.dp)
                    .clickableSingleWithNoRipple {
                        onSnsLoginClick(SNSType.NAVER)
                    },
                painter = painterResource(Res.drawable.ic_naver),
                contentDescription = "네이버 로그인"
            )

            Image(
                modifier = Modifier
                    .size(52.dp)
                    .clickableSingleWithNoRipple {
                        onSnsLoginClick(SNSType.GOOGLE)
                    },
                painter = painterResource(Res.drawable.ic_google),
                contentDescription = "구글 로그인"
            )

//            Image(
//                painter = painterResource(Res.drawable.ic_naver),
//                contentDescription = "애플 로그인"
//            )
        }
    }
}