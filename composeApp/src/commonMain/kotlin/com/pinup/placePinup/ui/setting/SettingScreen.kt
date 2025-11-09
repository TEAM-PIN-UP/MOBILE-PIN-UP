package com.pinup.placePinup.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.LocalPlatformContext
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.platform.openBrowser
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import com.pinup.placePinup.util.Const
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_chevron_right

@Composable
fun SettingScreen(
    email: String,
    snsType: SNSType?,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onChangedPassword: () -> Unit = {},
    onMoveUnRegister: () -> Unit = {}
) {
    val context = LocalPlatformContext.current
    val isShowLogoutDialog = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            title = Texts.Word.SETTING,
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        PHorizontalDivider()

        Column(
            modifier = Modifier
                .padding(vertical = 24.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = Texts.Setting.PROFILE_SETTING_TITLE,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray400
            )

            MenuBar(
                text = Texts.Setting.PROFILE_INFO,
                content = {
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "SNS 로그인(${snsType?.value})",
                            style = Typography.L1.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray400
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 2.dp),
                            text = email,
                            style = Typography.L1.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray400
                        )
                    }
                },
                onClick = {}
            )

            MenuBar(
                text = Texts.Setting.CHANGE_PASSWORD,
                onClick = {
                    if (snsType == SNSType.PINUP) onChangedPassword()
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = Texts.Setting.CS_TITLE,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray400
            )

            MenuBar(
                text = Texts.Setting.QA,
                onClick = {
                    openBrowser(
                        context = context,
                        url = Const.Url.SUGGESTION_URL
                    )
                }
            )

            MenuBar(
                text = Texts.Setting.SUGGEST,
                onClick = {
                    openBrowser(
                        context = context,
                        url = Const.Url.CONTACT_US_URL
                    )
                }
            )

            MenuBar(
                text = Texts.Setting.SERVICE_TERM,
                onClick = {
                    openBrowser(
                        context = context,
                        url = Const.Url.SERVICE_TERM
                    )
                }
            )

            MenuBar(
                text = Texts.Setting.PERSONAL_TERM,
                onClick = {
                    openBrowser(
                        context = context,
                        url = Const.Url.PERSONAL_TERM
                    )
                }
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                modifier = Modifier
                    .padding(start = 20.dp),
                text = Texts.Word.ETC,
                style = Typography.B3.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray400
            )

            MenuBar(
                text = Texts.Setting.VERSION,
                content = {
                    Text(
                        text = "v.1.0",
                        style = Typography.L1.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray400
                    )
                },
                onClick = {

                }
            )

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickableSingleWithNoRipple {
                        isShowLogoutDialog.value = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 20.dp),
                    text = Texts.Setting.LOGOUT,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray800
                )
            }

            PHorizontalDivider()

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clickableSingleWithNoRipple {
                        onMoveUnRegister()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 20.dp),
                    text = Texts.Setting.UNREGISTER,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray800
                )
            }
        }
    }

    if (isShowLogoutDialog.value) {
        PDialog(
            titleText = Texts.Setting.LOGOUT_DIALOG_TITLE,
            descriptionText = Texts.Setting.LOGOUT_DIALOG_DESCRIPTION,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Setting.LOGOUT,
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
            .padding(horizontal = 20.dp)
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
            style = Typography.B2.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Colors.Gray800
        )

        Spacer(Modifier.weight(1f))

        if (content == null) {
            Image(
                painter = painterResource(Res.drawable.ic_chevron_right),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Colors.Gray300)
            )
        } else {
            content()
        }
    }


    PHorizontalDivider()
}