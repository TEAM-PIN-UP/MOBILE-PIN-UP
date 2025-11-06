package com.pinup.placePinup.ui.reviewwrite.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.HalfStarRatingBar
import com.pinup.placePinup.ui.component.PButton
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.ReviewImage
import com.pinup.placePinup.ui.component.ReviewTextField
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun WriteReviewScreen(
    placeName: String,
    address: String,
    rating: Double,
    reviewCount: Int,
    reviewText: String,
    imagePaths: List<String>,
    myRating: Double,
    isEnableButton: Boolean,
    clickedImage: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onRatingSelected: (Double) -> Unit = {},
    onAddImage: (ByteArray) -> Unit = {},
    onRemoveImage: (String) -> Unit = {},
    onClickImage: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val minLength = 10
    val maxLength = 10000
    val maxImageSize = 3
    val scope = rememberCoroutineScope()
    var isShowImageDetailDialog by remember { mutableStateOf(false) }
    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                onAddImage(it)
            }
        }
    )
    val focusManager = LocalFocusManager.current

    if (isShowImageDetailDialog) {
        Dialog(
            onDismissRequest = {
                isShowImageDetailDialog = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 20.dp)
                        .clickableWithNoRipple {
                            isShowImageDetailDialog = false
                        },
                    painter = painterResource(Res.drawable.ic_cancel_white),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth(),
                    model = clickedImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            title = Texts.PinLog.WRITE_PINLOG,
            onLeftButtonClick = onBackPressed
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = placeName,
                        style = Typography.T1.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray900,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = address,
                        style = Typography.B3.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray400,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            modifier = Modifier
                                .size(18.dp),
                            painter = painterResource(Res.drawable.ic_star),
                            contentDescription = "rating"
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Text(
                            text = rating.toString(),
                            style = Typography.B2.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray900
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = Texts.Word.PINLOG,
                            style = Typography.L1.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray700
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Text(
                            text = reviewCount.toString(),
                            style = Typography.L1.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray700
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                PHorizontalDivider(
                    modifier = Modifier
                        .padding(end = 40.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .padding(start = 20.dp)
                ) {
                    Text(
                        text = Texts.PinLog.IMAGE_UPLOAD,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray800,
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(imagePaths) {
                            Box {
                                ReviewImage(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(top = 8.dp, end = 7.dp),
                                    imgUrl = it,
                                    onClickImage = {
                                        onClickImage(it)
                                        isShowImageDetailDialog = true
                                    }
                                )

                                Image(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .align(Alignment.TopEnd)
                                        .clickableWithNoRipple {
                                            onRemoveImage(it)
                                        },
                                    painter = painterResource(Res.drawable.ic_remove),
                                    contentDescription = "image remove"
                                )
                            }
                        }

                        if (imagePaths.size < maxImageSize) {
                            item {
                                Box {
                                    RoundedBox(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(top = 8.dp, end = 7.dp)
                                            .clickableSingleWithNoRipple {
                                                singleImagePicker.launch()
                                            },
                                        cornerRounded = 8,
                                        backgroundColor = Colors.Gray50,
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                        ) {
                                            Image(
                                                painter = painterResource(Res.drawable.ic_camera_gray),
                                                contentDescription = "select image",
                                            )

                                            Spacer(modifier = Modifier.height(5.dp))

                                            Text(
                                                text = "${imagePaths.size}/$maxImageSize",
                                                style = Typography.L1.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = Colors.Gray300,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .align(Alignment.TopEnd)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                PHorizontalDivider(
                    modifier = Modifier
                        .padding(end = 40.dp)
                )

            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = Texts.Word.RATING,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray800,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HalfStarRatingBar(
                        rating = myRating,
                        size = 18.dp,
                        spacing = 4.dp,
                        onRatingChanged = onRatingSelected
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                PHorizontalDivider(
                    modifier = Modifier
                        .padding(end = 40.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = Texts.PinLog.WRITE_PINLOG_TITLE,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray800,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ReviewTextField(
                        text = reviewText,
                        textColor = Colors.Gray800,
                        textStyle = Typography.B3.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        placeholder = Texts.PinLog.PINLOG_HINT,
                        onValueChange = onValueChange,
                        maxLength = maxLength,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Default,
                            keyboardType = KeyboardType.Text,
                        )
                    )

                    if (reviewText.length < minLength || reviewText.length > maxLength) {
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = if (reviewText.length < minLength) Texts.PinLog.PINLOG_MORE_LENGTH else Texts.PinLog.PINLOG_TOO_MUCH_LENGTH,
                            style = Typography.L1.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Gray400
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .background(color = Colors.White)
        ) {
            PHorizontalDivider()

            PButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp, bottom = 14.dp),
                text = Texts.PinLog.REGISTER_PINLOG,
                onClick = {
                    onRegisterClick()
                },
                isEnable = isEnableButton
            )

            Spacer(modifier = Modifier.height(42.dp))
        }
    }
}