package com.pinup.placePinup.ui.profilesetting
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.CommentMenuBottomSheet
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.PinlogMenuBottomSheet
import com.pinup.placePinup.ui.component.RoundedTextField
import com.pinup.placePinup.ui.component.bottomSheet.ProfileMenuBottomSheet
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_back
import pinup.composeapp.generated.resources.ic_profile_select
import pinup.composeapp.generated.resources.ic_profile_setting_image

@Composable
fun ProfileSettingScreen(
    onBackPressed: () -> Unit,
    onClickModifyProfile: () -> Unit = {},
    onUpdateProfileImage: (ByteArray?) -> Unit,
    profileImage: String = "",
    profileImageByte: ByteArray? = null,
    nickName: String = "",
    onNickNameChange: (String) -> Unit = {},
    bio: String = "",
    onBioChange: (String) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                onUpdateProfileImage(it)
            }
        }
    )
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            ProfileMenuBottomSheet(
                onClickSelectImage = {
                    singleImagePicker.launch()
                    scope.launch { sheetState.hide() }
                },
                onClickDelete = {
                    onUpdateProfileImage(null)
                    scope.launch { sheetState.hide() }
                },
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .background(Colors.White)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                        keyboard?.hide()
                    })
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
                    .height(56.dp),
            ) {
                Image(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onBackPressed()
                        }
                        .align(Alignment.CenterStart),
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = null
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Center),
                    text =  stringResource(Res.string.setting_profile_setting),
                    style = Typography.H3,
                    color = Colors.Neutral800
                )

                Text(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickModifyProfile()
                        }
                        .align(Alignment.CenterEnd),
                    text = stringResource(Res.string.word_complete),
                    color = Colors.Main,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            PHorizontalDivider()

            Spacer(modifier = Modifier.height(42.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                if (profileImage.isEmpty() && profileImageByte == null) {
                    Image(
                        modifier = Modifier
                            .size(84.dp)
                            .clickableSingleWithNoRipple {
                                scope.launch { sheetState.show() }
                                //singleImagePicker.launch()
                            },
                        painter = painterResource(Res.drawable.ic_profile_select),
                        contentDescription = null,
                    )
                } else {
                    Box {
                        AsyncImage(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(84.dp),
                            model = profileImageByte ?: profileImage,
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                        )

                        Image(
                            modifier = Modifier
                                .size(84.dp)
                                .clickableSingleWithNoRipple {
                                    scope.launch { sheetState.show() }
                                    //singleImagePicker.launch()
                                },
                            painter = painterResource(Res.drawable.ic_profile_setting_image),
                            contentDescription = null,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.word_nickname),
                        color = Colors.Gray800,
                        style = Typography.B1.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )

                    Spacer(modifier = Modifier.height(36.dp))

                    Text(
                        text = stringResource(Res.string.word_intro),
                        color = Colors.Gray800,
                        style = Typography.B1.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    RoundedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = nickName,
                        onValueChange = {
                            onNickNameChange(it)
                        },
                        cornerRounded = 0,
                        textStyle = Typography.B1.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        contentPadding = PaddingValues(0.dp),
                        fixedBorderColor = Colors.Transparency
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PHorizontalDivider(
                        color = if (nickName.isEmpty()) Colors.Gray100 else Colors.Gray800
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    RoundedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = bio,
                        onValueChange = {
                            onBioChange(it)
                        },
                        cornerRounded = 0,
                        singleLine = false,
                        textStyle = Typography.B1.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        contentPadding = PaddingValues(0.dp),
                        fixedBorderColor = Colors.Transparency,
                        placeholder = stringResource(Res.string.setting_hint_bio_change)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    PHorizontalDivider(
                        color = if (bio.isEmpty()) Colors.Gray100 else Colors.Gray800
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                text = stringResource(Res.string.setting_hint_nickname_change),
                color = Colors.Gray400,
                style = Typography.L1.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}