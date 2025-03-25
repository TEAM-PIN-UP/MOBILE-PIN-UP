package com.pinup.pinup.ui.login.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pinup.pinup.R
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.theme.Colors


@Composable
fun LoginScreen(
    onSnsLoginClick: (SNSType) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(top = 138.dp),
            text = "‘찐’친들과 공유하는 나만의 ‘찐’ 맛집",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.W600,
                color = Colors.White
            )
        )

        Spacer(
            modifier = Modifier
                .weight(1f)
        )

        Image(
            modifier = Modifier
                .padding(bottom = 18.dp),
            painter = painterResource(Res.drawable.ic_sns_login),
            contentDescription = "sns 로그인 시작 팝업"
        )

        Row(
            modifier = Modifier
                .padding(bottom = 190.dp)
                .padding(horizontal = 66.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .clickableSingleWithNoRipple {
                        onSnsLoginClick(SNSType.KAKAO)
                    },
                painter = painterResource(Res.drawable.ic_kakao),
                contentDescription = "카카오 로그인"
            )

            Image(
                modifier = Modifier
                    .clickableSingleWithNoRipple {
                        onSnsLoginClick(SNSType.NAVER)
                    },
                painter = painterResource(Res.drawable.ic_naver),
                contentDescription = "네이버 로그인"
            )

            Image(
                modifier = Modifier
                    .size(60.dp)
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


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    PinUPTheme {
        LoginScreen()
    }
}