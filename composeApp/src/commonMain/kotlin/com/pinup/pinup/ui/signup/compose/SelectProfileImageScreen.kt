package com.pinup.pinup.ui.signup.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.theme.Texts

@Composable
fun SelectProfileImageScreen(
    profileImage: ByteArray,
    nickname: String,
    onUpdateProfileImage: (ByteArray) -> Unit,
    onClickSignup: () -> Unit,
    onBackPressed: () -> Unit,
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
        TitleBar(
            onLeftButtonClick = {
                onBackPressed()
            }
        )

        Spacer(modifier = Modifier.height(49.dp))

        Text(
            text = if (profileImage.isEmpty()) Texts.SignupProfile.PROFILE_TITLE else Texts.SignupProfile.PROFILE_REGISTER,
            style = Typography.D2.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Colors.Gray800
        )

        Spacer(modifier = Modifier.height(107.dp))

        Column(
            modifier = Modifier
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
            }

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = nickname,
                style = Typography.B1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Black
            )
        }

        Spacer(Modifier.weight(1f))

        if (profileImage.isEmpty()) {
            PButton(
                text = Texts.SignupProfile.PROFILE_SELECT,
                onClick = {
                    singleImagePicker.launch()
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickableSingleWithNoRipple {
                        onClickSignup()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 15.dp),
                    text = Texts.Word.SKIP,
                    style = Typography.B2.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Colors.Gray600
                )
            }
        } else {
            PButton(
                modifier = Modifier,
                text = Texts.Word.NEXT,
                onClick = {
                    onClickSignup()
                }
            )
        }

        Spacer(modifier = Modifier.height(53.dp))
    }
}