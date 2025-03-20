package com.pinup.pinup.ui.signup.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography

@Composable
fun InputNameScreen(
    nickname: String,
    isNicknameUsed: Boolean?,
    onValueChange: (String) -> Unit,
    onMoveSelectProfileImage: () -> Unit,
) {
    val maxLength = 12
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = "반가워요!",
            style = Typography.H1,
            color = Colors.Neutral800
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = "닉네임을 만들어볼까요?",
            style = Typography.H1,
            color = Colors.Neutral800
        )

        Text(
            modifier = Modifier
                .padding(top = 12.dp),
            text = "닉네임은 나중에 언제든지 변경가능해요.",
            style = Typography.B3,
            color = Colors.Neutral500
        )

        RoundedTextField(
            modifier = Modifier
                .padding(top = 24.dp),
            text = nickname,
            onValueChange = {
                if (it.length > maxLength) return@RoundedTextField
                onValueChange(it)
            },
            cornerRounded = 100,
            placeholder = "닉네임 입력",
            unfocusedBorderColor = Colors.Neutral200,
            focusedBorderColor = if (isNicknameUsed == true) Colors.Error else Colors.Neutral800
        )

        Row() {
            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = if (isNicknameUsed == true) "중복되는 닉네임이에요." else "한글, 영문만 입력 가능",
                style = Typography.B5,
                color = if (isNicknameUsed == true) Colors.Error else Colors.Neutral500
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            Text(
                modifier = Modifier
                    .padding(top = 12.dp),
                text = "${nickname.length} / $maxLength",
                style = Typography.B5,
                color = if (isNicknameUsed == true) Colors.Error else Colors.Neutral500
            )
        }

        PButton(
            modifier = Modifier
                .padding(top = 20.dp),
            text = "다음",
            isEnable = isNicknameUsed == false,
            onClick = {
                onMoveSelectProfileImage()
            }
        )
    }
}
