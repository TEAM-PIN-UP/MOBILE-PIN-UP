package com.pinup.pinup.ui.findAccount.findId

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.CommentMenuBottomSheet
import com.pinup.pinup.ui.component.ErrorText
import com.pinup.pinup.ui.component.FindIdBottomSheet
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.PinlogMenuBottomSheet
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.signup.EmailVerifyType
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_check_circle_black

@Composable
fun FindIdScreen(
    email: String = "",
    verificationCode: String = "",
    isEmailValid: Boolean = false,
    isVerifyClicked: Boolean = false,
    isEmailFindClicked: Boolean = true,
    findProfileUrl: String = "",
    findNickName: String = "",
    emailVerifyType: EmailVerifyType = EmailVerifyType.NONE,
    nickname: String = "",
    isNicknameUsed: Boolean = false,
    timer: String = "",
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    onClickSendCode: () -> Unit = {},
    onClickConfirm: () -> Unit,
    onClickLogin: () -> Unit = {},
    onClickFindPassword: () -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onNickNameChanged: (String) -> Unit = {},
    onCodeChanged: (String) -> Unit = {},
    onClickTabChanged: (Boolean) -> Unit = {},
    onBackPressed: () -> Unit,
) {
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            FindIdBottomSheet(
                profileUrl = findProfileUrl,
                nickname = findNickName,
                onClickLogin = onClickLogin,
                onClickFindPassword = onClickFindPassword
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.White)
                .padding(horizontal = 20.dp),
        ) {
            TitleBar(
                onLeftButtonClick = onBackPressed
            )

            Spacer(modifier = Modifier.height(44.dp))

            Text(
                text = Texts.FindId.FIND_ID,
                style = Typography.D2.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Colors.Gray800
            )

            Spacer(modifier = Modifier.height(25.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                RoundedBox(
                    modifier = Modifier
                        .weight(1f),
                    backgroundColor = if (isEmailFindClicked) Colors.Gray800 else Colors.White,
                    cornerColor = if (isEmailFindClicked) Colors.Transparency else Colors.Gray300,
                    cornerRounded = 999,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickableWithNoRipple {
                                onClickTabChanged(true)
                            },
                        text = Texts.FindId.FIND_ID_BY_EMAIL,
                        color = if (isEmailFindClicked) Colors.White else Colors.Gray300,
                        style = Typography.L2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                RoundedBox(
                    modifier = Modifier
                        .weight(1f),
                    cornerRounded = 999,
                    backgroundColor = if (isEmailFindClicked) Colors.White else Colors.Gray800,
                    cornerColor = if (isEmailFindClicked) Colors.Gray300 else Colors.Transparency,
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickableWithNoRipple {
                                onClickTabChanged(false)
                            },
                        text = Texts.FindId.FIND_ID_BY_NICKNAME,
                        color = if (isEmailFindClicked) Colors.Gray300 else Colors.White,
                        style = Typography.L2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isEmailFindClicked) {
                Text(
                    text = Texts.Word.EMAIL,
                    style = Typography.B1.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray700
                )

                Spacer(modifier = Modifier.height(3.dp))

                Box {
                    RoundedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = email,
                        onValueChange = onEmailChanged,
                        placeholder = Texts.SignupEmail.HINT,
                        cornerRounded = 100,
                        backgroundColor = Colors.White,
                        isError = !isEmailValid,
                    )

                    Row(
                        modifier = Modifier
                            .padding(top = 11.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RoundedBox(
                            modifier = Modifier
                                .clickableSingleWithNoRipple {
                                    if (isEmailValid && !isVerifyClicked) onClickSendCode()
                                },
                            cornerRounded = 100,
                            backgroundColor = if (isEmailValid && !isVerifyClicked) Colors.Gray800 else Colors.Gray300,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .align(Alignment.Center),
                                text = Texts.Word.VERIFY,
                                style = Typography.L3.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Colors.White
                            )
                        }

                        Spacer(modifier = Modifier.width(19.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!isEmailValid) {
                    ErrorText(Texts.SignupEmail.INVALID)

                    Spacer(modifier = Modifier.height(8.dp))
                }

                Box {
                    RoundedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = verificationCode,
                        onValueChange = onCodeChanged,
                        placeholder = Texts.SignupEmail.CODE_HINT,
                        cornerRounded = 100,
                        backgroundColor = Colors.White,
                        isError = emailVerifyType == EmailVerifyType.NOT_VERIFIED,
                    )

                    Row(
                        modifier = Modifier
                            .padding(vertical = 17.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = timer,
                            style = Typography.L3.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Gray300
                        )

                        Spacer(modifier = Modifier.width(17.dp))
                    }
                }

                if (emailVerifyType == EmailVerifyType.NOT_VERIFIED) {
                    Spacer(modifier = Modifier.height(8.dp))
                    ErrorText(Texts.SignupEmail.CODE_INVALID)
                }
            } else {
                Text(
                    text = Texts.FindId.INPUT_NICKNAME,
                    style = Typography.B1.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray700
                )

                Spacer(modifier = Modifier.height(3.dp))

                RoundedTextField(
                    modifier = Modifier,
                    text = nickname,
                    onValueChange = {
                        onNickNameChanged(it)
                    },
                    cornerRounded = 100,
                    placeholder = Texts.SignupNickname.NICKNAME_HINT,
                    isError = !isNicknameUsed && nickname.isNotEmpty(),
                    tailIcon = if(!isNicknameUsed) null else painterResource(Res.drawable.ic_check_circle_black)
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (!isNicknameUsed && nickname.isNotEmpty()) {
                    ErrorText(
                        text = Texts.FindId.NOT_EXIST_NICKNAME
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            PButton(
                modifier = Modifier
                    .padding(bottom = 28.dp),
                text = Texts.Word.CONFIRM,
                onClick = {
                    onClickConfirm()
                },
                isEnable = verificationCode.length == 6 || isNicknameUsed
            )

            Spacer(modifier = Modifier.height(53.dp))
        }
    }
}