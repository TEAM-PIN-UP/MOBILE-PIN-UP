package com.pinup.pinup.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.openBrowser
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import com.pinup.pinup.util.Const
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun SettingScreen(
    email: String,
    snsType: SNSType?,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val context = LocalPlatformContext.current
//    val packageInfo = remember {
//        context.packageManager.getPackageInfo(context.packageName, 0)
//    }
    val isShowLogoutDialog = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        TitleBar(
            title = "설정",
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        PHorizontalDivider()

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 30.dp)
        ) {
            Text(
                text = "계정 설정",
                style = Typography.H5,
                color = Colors.Neutral400
            )

            MenuBar(
                text = "프로필 편집",
                onClick = {}
            )
            PHorizontalDivider()
            MenuBar(
                text = "계정 정보",
                content = {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "SNS 로그인(${snsType?.value})",
                            style = Typography.H6,
                            color = Colors.Neutral400
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 2.dp),
                            text = email,
                            style = Typography.H6,
                            color = Colors.Neutral400
                        )
                    }
                },
                onClick = {}
            )
            PHorizontalDivider()

            Text(
                modifier = Modifier
                    .padding(top = 28.dp),
                text = "고객센터",
                style = Typography.H5,
                color = Colors.Neutral400
            )
            MenuBar(
                text = "문의하기",
                onClick = {
                    openBrowser(
                        context = context,
                        url = Const.Url.SUGGESTION_URL
                    )
                }
            )
            PHorizontalDivider()
            MenuBar(
                text = "건의하기",
                onClick = {
                    openBrowser(
                        url = Const.Url.CONTACT_US_URL,
                        context = context
                    )
                }
            )
            PHorizontalDivider()
            MenuBar(
                text = "이용약관",
                onClick = {}
            )
            PHorizontalDivider()
            MenuBar(
                text = "개인정보 처리방침",
                onClick = {}
            )
            PHorizontalDivider()

            Text(
                modifier = Modifier
                    .padding(top = 28.dp),
                text = "기타",
                style = Typography.H5,
                color = Colors.Neutral400
            )
            MenuBar(
                text = "앱버전",
                content = {
                    Text(
                        text = "V ${1.0}",
                        style = Typography.H6,
                        color = Colors.Neutral400
                    )
                },
                onClick = {

                }
            )
            PHorizontalDivider()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
                    .clickableWithNoRipple {
                        isShowLogoutDialog.value = true
                    },
                text = "로그아웃",
                style = Typography.H4,
                color = Colors.Neutral800
            )
            PHorizontalDivider()
        }
    }

    if (isShowLogoutDialog.value) {
        PDialog(
            titleText = "로그아웃 하시겠어요?",
            descriptionText = "아쉬워요 \uD83D\uDE25\n언제든 다시 놀러오세요!",
            leftButtonText = "취소",
            rightButtonText = "로그아웃",
            onLeftButtonClick = {
                isShowLogoutDialog.value = false
            },
            onRightButtonClick = {
                isShowLogoutDialog.value = false
                onLogoutClick()
            },
        )
    }
}

@Composable
private fun MenuBar(
    text: String,
    content: @Composable (() -> Unit)? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableSingleWithNoRipple {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 20.dp),
            text = text,
            style = Typography.H4,
            color = Colors.Neutral800
        )

        Spacer(Modifier.weight(1f))

        if (content == null) {
            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Colors.Neutral300)
            )
        } else {
            content()
        }
    }
}