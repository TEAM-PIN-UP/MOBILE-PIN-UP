package com.pinup.pinup.ui.signup.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun SelectProfileImageScreen(
    profileImage: ByteArray,
    nickname: String,
    onUpdateProfileImage: (ByteArray) -> Unit,
    onMoveTermsOfService: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                onUpdateProfileImage(it)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Colors.White)
            .padding(horizontal = 20.dp)
    ) {
        Text(
            modifier = Modifier.padding(top = 40.dp),
            text = if (profileImage.isEmpty()) "프로필에 사용 될" else "멋진 프로필",
            style = Typography.H1,
            color = Colors.Neutral800
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = if (profileImage.isEmpty()) "이미지를 골라주세요." else "사진이 등록되었어요!",
            style = Typography.H1,
            color = Colors.Neutral800
        )

        Column(
            modifier = Modifier
                .padding(top = 64.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (profileImage.isEmpty()) {
                Image(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            singleImagePicker.launch()
                        },
                    painter = painterResource(Res.drawable.ic_profile_select),
                    contentDescription = null,
                )
            } else {
                Box {
                    AsyncImage(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp)
                            .clickableSingleWithNoRipple {
                                singleImagePicker.launch()
                            },
                        model = profileImage,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(100.dp)
                            .background(Colors.Black_40),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_camera),
                            contentDescription = null
                        )
                    }
                }
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = nickname,
                style = Typography.B3,
                color = Colors.Neutral800
            )
        }

        Spacer(Modifier.weight(1f))

        if (profileImage.isEmpty()) {
            PButton(
                text = "사진 선택하기",
                onClick = {
                    singleImagePicker.launch()
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 13.dp)
                    .clickableSingleWithNoRipple {
                        onMoveTermsOfService()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    text = "건너뛰기",
                    style = Typography.H4,
                    color = Colors.Neutral400
                )
            }
        } else {
            PButton(
                modifier = Modifier
                    .padding(bottom = 28.dp),
                text = "다음",
                onClick = {
                    onMoveTermsOfService()
                }
            )
        }
    }
}
