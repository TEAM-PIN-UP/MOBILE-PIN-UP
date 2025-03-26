package com.pinup.pinup.ui.reviewwrite.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.HalfStarRatingBar
import com.pinup.pinup.ui.component.PButton
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.ReviewDialog
import com.pinup.pinup.ui.component.ReviewImage
import com.pinup.pinup.ui.component.ReviewTextField
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
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
    imagePaths: List<ByteArray>,
    myRating: Int,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onRatingSelected: (Int) -> Unit = {},
    onAddImage: (ByteArray) -> Unit = {},
    onRemoveImage: (ByteArray) -> Unit = {},
    onRegisterClick: () -> Unit = {},
) {
    val maxImageSize = 3
    val scrollState = rememberScrollState()
    val isShowRatingDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val singleImagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let {
                onAddImage(it)
            }
        }
    )

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Colors.White
            )
    ) {
        val (content, cta) = createRefs()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .constrainAs(content) {
                    linkTo(top = parent.top, bottom = cta.top, bias = 0f)
                    height = Dimension.fillToConstraints
                }
                .verticalScroll(
                    state = scrollState
                ),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = placeName,
                    style = Typography.H2,
                    color = Colors.Neutral800,
                )

                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = address,
                    style = Typography.B4,
                    color = Colors.Neutral400,
                )

                Row (
                    modifier = Modifier
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(Res.drawable.ic_star),
                        contentDescription = "rating"
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 2.dp),
                        text = rating.toString(),
                        style = Typography.H4,
                        color = Colors.Neutral800
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 6.dp),
                        text = "리뷰 $reviewCount",
                        style = Typography.B4,
                        color = Colors.Neutral700
                    )
                }
            }

            PHorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "사진 업로드",
                    style = Typography.H4,
                    color = Colors.Neutral800,
                )

                LazyRow(
                    modifier = modifier
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (imagePaths.size < maxImageSize) {
                        item {
                            RoundedBox(
                                modifier = modifier
                                    .size(100.dp)
                                    .clickableSingleWithNoRipple {
                                        singleImagePicker.launch()
                                    },
                                cornerRounded = 8,
                                backgroundColor = Colors.Neutral100,
                            ) {
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                ) {
                                    Image(
                                        painter = painterResource(Res.drawable.ic_camera_gray),
                                        contentDescription = "select image",
                                    )

                                    Text(
                                        modifier = Modifier
                                            .padding(top = 5.dp),
                                        text = "${imagePaths.size} / $maxImageSize",
                                        style = Typography.H5,
                                        color = Colors.Neutral300,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    items(imagePaths) {
                        Box {
                            ReviewImage(
                                imgUrl = it
                            )

                            Image(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(top = 5.dp, end = 5.dp)
                                    .clickableWithNoRipple {
                                        onRemoveImage(it)
                                    },
                                painter = painterResource(Res.drawable.ic_remove),
                                contentDescription = "image remove"
                            )
                        }
                    }
                }
            }

            PHorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "별점",
                    style = Typography.H4,
                    color = Colors.Neutral800,
                )

                Row(
                    modifier = Modifier
                        .padding(top = 12.dp)
                ) {
                    HalfStarRatingBar(
                        rating = myRating,
                        size = 26.dp,
                        spacing = 6.dp,
                        onRatingChanged = {
                            isShowRatingDialog.value = true
                        }
                    )
                }
            }

            PHorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 20.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 100.dp)
            ) {
                Text(
                    text = "리뷰 작성하기",
                    style = Typography.H4,
                    color = Colors.Neutral800,
                )

                ReviewTextField(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = reviewText,
                    placeholder = "작성 된 리뷰는 나의 친구들에게만 보여요\n\n" +
                            "*주의: 욕설, 비방 목적 혹은 명예 훼손성 내용은 작성 시 삭제 처리 될 수 있습니다.",
                    onValueChange = onValueChange,
                    maxLength = 400
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(cta) {
                    bottom.linkTo(parent.bottom)
                }
        ) {

            PHorizontalDivider()

            PButton(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(top = 8.dp, bottom = 14.dp),
                text = "리뷰 등록하기",
                onClick = {
                    onRegisterClick()
                }
            )
        }
    }

    if (isShowRatingDialog.value) {
        ReviewDialog(
            rating = myRating,
            onDismissRequest = {
                isShowRatingDialog.value = false
            },
            onConfirmClick = {
                isShowRatingDialog.value = false
                onRatingSelected(it)
            }
        )
    }
}