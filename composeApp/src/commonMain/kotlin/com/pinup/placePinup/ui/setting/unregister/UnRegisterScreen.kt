package com.pinup.placePinup.ui.setting.unregister
import org.jetbrains.compose.resources.stringResource

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_check_rect_off
import pinup.composeapp.generated.resources.ic_check_rect_on

@Composable
fun UnRegisterScreen(
    isCheck: Boolean = false,
    onBackPressed: () -> Unit = {},
    onUnRegisterClick: () -> Unit = {},
    onClickCheck:() -> Unit = {},
) {
    val isShowUnRegisterDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            title = stringResource(Res.string.setting_unregister),
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        PHorizontalDivider()

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(57.dp))

            Text(
                text = stringResource(Res.string.setting_unregister_title),
                style = Typography.D2.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Colors.Gray800
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.setting_unregister_description),
                style = Typography.T2.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Colors.Gray700
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickCheck()
                        },
                    painter = painterResource(if (isCheck) Res.drawable.ic_check_rect_on else Res.drawable.ic_check_rect_off),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = stringResource(Res.string.setting_unregister_check_comment),
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray500
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            PButton(
                text = stringResource(Res.string.word_unregister),
                onClick = {
                    isShowUnRegisterDialog.value = true
                },
                isEnable = isCheck
            )

            Spacer(modifier = Modifier.height(42.dp))
        }
    }

    if (isShowUnRegisterDialog.value) {
        PDialog(
            titleText = stringResource(Res.string.setting_unregister_dialog_title),
            descriptionText = stringResource(Res.string.setting_unregister_dialog_description),
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_unregister),
            onLeftButtonClick = {
                isShowUnRegisterDialog.value = false
            },
            onRightButtonClick = {
                isShowUnRegisterDialog.value = false
                onUnRegisterClick()
            },
        )
    }
}
