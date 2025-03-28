package com.pinup.pinup.ui.signup.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun CompleteScreen(
    onSignUpClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                text = "반가워요!",
                style = Typography.H3,
                color = Colors.Neutral800
            )

            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "이제 ‘찐’ 맛집 리뷰를 탐색해볼까요?",
                style = Typography.H3,
                color = Colors.Neutral800
            )
        }

        PButton(
            modifier = Modifier
                .padding(bottom = 28.dp),
            text = "핀업 시작하기",
            onClick = {
                onSignUpClick()
            }
        )
    }
}
