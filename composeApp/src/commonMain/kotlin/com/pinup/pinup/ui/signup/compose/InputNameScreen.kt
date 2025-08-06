package com.pinup.pinup.ui.signup.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_error_circle
import pinup.composeapp.generated.resources.ic_error_circle_red

@Composable
fun InputNameScreen(
    nickname: String,
    isNicknameUsed: Boolean,
    onValueChange: (String) -> Unit,
    onMoveSelectProfileImage: () -> Unit,
    onBackPressed: () -> Unit,
    isPassNickname: Boolean
) {
    val maxLength = 12
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

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = Texts.SignupNickname.NICKNAME_TITLE,
            style = Typography.H0,
            color = Colors.Black
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = Texts.SignupNickname.INPUT_NICKNAME,
            style = Typography.H3,
            color = Colors.Neutral700
        )

        Spacer(modifier = Modifier.height(8.dp))

        RoundedTextField(
            modifier = Modifier,
            text = nickname,
            onValueChange = {
                if (it.length > maxLength) return@RoundedTextField
                onValueChange(it)
            },
            cornerRounded = 100,
            placeholder = Texts.SignupNickname.NICKNAME_HINT,
            textStyle = Typography.B3,
            isError = isNicknameUsed,
            unfocusedBorderColor = Colors.Neutral200,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                modifier = Modifier.padding(top = 4.dp),
                painter = painterResource(if(isNicknameUsed) Res.drawable.ic_error_circle_red else Res.drawable.ic_error_circle),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                modifier = Modifier
                    .padding(top = 4.dp),
                text = if (isNicknameUsed) Texts.SignupNickname.DUPLICATE_NICKNAME else Texts.SignupNickname.CONDITION_NICKNAME,
                style = Typography.B5,
                color = if (isNicknameUsed) Colors.Error else Colors.Neutral500
            )

            Spacer(
                modifier = Modifier
                    .weight(1f)
            )

            Text(
                text = "${nickname.length}/$maxLength",
                style = Typography.B5,
                color = if (isNicknameUsed == true) Colors.Error else Colors.Neutral500
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PButton(
            text = Texts.Word.NEXT,
            isEnable = isPassNickname,
            onClick = {
                onMoveSelectProfileImage()
            }
        )

        Spacer(modifier = Modifier.height(53.dp))
    }
}