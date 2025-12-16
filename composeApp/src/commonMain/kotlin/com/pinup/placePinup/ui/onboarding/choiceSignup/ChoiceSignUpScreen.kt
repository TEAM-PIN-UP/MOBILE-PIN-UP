
package com.pinup.placePinup.ui.onboarding.choiceSignup

import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.platform.PLATFORM_IOS
import com.pinup.placePinup.platform.getPlatformName
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_apple_login
import pinup.composeapp.generated.resources.ic_email_login
import pinup.composeapp.generated.resources.ic_google_login
import pinup.composeapp.generated.resources.ic_kakao_login
import pinup.composeapp.generated.resources.ic_naver_login

@Composable
fun ChoiceSignUpScreen(
    onClickSnsLogin : (SNSType) -> Unit = { },
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

        Image(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clickableSingleWithNoRipple {
                    onClickSnsLogin(SNSType.KAKAO)
                },
            painter = painterResource(Res.drawable.ic_kakao_login),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clickableSingleWithNoRipple {
                    onClickSnsLogin(SNSType.NAVER)
                },
            painter = painterResource(Res.drawable.ic_naver_login),
            contentDescription = null
        )

//        Spacer(modifier = Modifier.height(20.dp))
//
//        Image(
//            modifier = Modifier
//                .padding(horizontal = 20.dp)
//                .fillMaxWidth()
//                .clickableSingleWithNoRipple {
//                    onClickSnsLogin(SNSType.GOOGLE)
//                },
//            painter = painterResource(Res.drawable.ic_google_login),
//            contentDescription = null
//        )

        if (getPlatformName() == PLATFORM_IOS) {
            Spacer(modifier = Modifier.height(20.dp))

            Image(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickableSingleWithNoRipple {
                        onClickSnsLogin(SNSType.APPLE)
                    },
                painter = painterResource(Res.drawable.ic_apple_login),
                contentDescription = null
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .clickableSingleWithNoRipple {
                    onClickSnsLogin(SNSType.PINUP)
                },
            painter = painterResource(Res.drawable.ic_email_login),
            contentDescription = null
        )
    }
}