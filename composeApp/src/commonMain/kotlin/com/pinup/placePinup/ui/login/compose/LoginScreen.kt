package com.pinup.placePinup.ui.login.compose

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.platform.PLATFORM_IOS
import com.pinup.placePinup.platform.getPlatformName
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.RoundedTextField
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_apple
import pinup.composeapp.generated.resources.ic_google
import pinup.composeapp.generated.resources.ic_kakao
import pinup.composeapp.generated.resources.ic_naver


@Composable
fun LoginScreen(
    onSnsLoginClick: (SNSType) -> Unit = {},
    onClickFindPassword: () -> Unit = {},
    onCLickFindEmail: () -> Unit = {},
    onMoveSignUp: () -> Unit = {},
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
            style = Typography.D2
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier,
            text = Texts.Onboarding.ONBOARDING_LOGIN_TEXT,
            style = Typography.B1.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Colors.Gray400
        )

        Spacer(modifier = Modifier.height(49.dp))

        RoundedTextField(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth(),
            text = id,
            onValueChange = onIdChanged,
            placeholder = Texts.Word.WORD_ID,
            textStyle = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
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
            textStyle = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
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
                onSnsLoginClick(SNSType.PINUP)
            }
        )

        if (isError) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier,
                text = Texts.Login.INCORRECT_ID,
                style = Typography.B2.copy(
                    fontWeight = FontWeight.Medium
                ),
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
                modifier = Modifier
                    .clickableSingleWithNoRipple {
                        onCLickFindEmail()
                    },
                text = Texts.Login.LOGIN_ID_FIND,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray700
            )

            Spacer(modifier = Modifier.width(23.dp))

            Text(
                modifier = Modifier
                    .clickableSingleWithNoRipple {
                        onClickFindPassword()
                    },
                text = Texts.FindPassword.CHANGE_PASSWORD,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray700
            )

            Spacer(modifier = Modifier.width(23.dp))

            Text(
                modifier = Modifier
                    .clickableSingleWithNoRipple {
                        onMoveSignUp()
                    },
                text = Texts.Word.SIGN_UP,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray700
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
                style = Typography.B3.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray400
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

//            Image(
//                modifier = Modifier
//                    .size(52.dp)
//                    .clickableSingleWithNoRipple {
//                        onSnsLoginClick(SNSType.GOOGLE)
//                    },
//                painter = painterResource(Res.drawable.ic_google),
//                contentDescription = "구글 로그인"
//            )

            if (getPlatformName() == PLATFORM_IOS) {
                Image(
                    modifier = Modifier
                        .size(52.dp)
                        .clickableSingleWithNoRipple {
                            onSnsLoginClick(SNSType.APPLE)
                        },
                    painter = painterResource(Res.drawable.ic_apple),
                    contentDescription = "애플 로그인"
                )
            }
        }
    }
}