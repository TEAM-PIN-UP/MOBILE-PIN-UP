package com.pinup.pinup.ui.userprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.model.PintsPageAble
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.extentions.ScrollToEndCallback
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.FeedView
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PVerticalDivider
import com.pinup.pinup.ui.component.PinBuddyBottomSheet
import com.pinup.pinup.ui.component.PinchItemView
import com.pinup.pinup.ui.component.ProfileImageView
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun UserProfileScreen(
    member: Member,
    photoReviews: List<Review>,
    pinchPageAble: PintsPageAble,
    modifier: Modifier = Modifier,
    onRequestPinBuddy: () -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onRequestCancel: () -> Unit = {},
    onRemovePinBuddy: () -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
    onClickDetail: (Int) -> Unit = {},
    onClickShare: () -> Unit = {},
    onMovePinchWrite: () -> Unit = {},
    getMorePints: () -> Unit = {},
    onMoveDetail: (Int) -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var isShowDeleteDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val pagerState = remember { mutableStateOf(0) }
    val scrollState = rememberLazyListState()

    ScrollToEndCallback(scrollState) {
        if (pagerState.value == 1 && !pinchPageAble.last) {
            getMorePints()
        }
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            PinBuddyBottomSheet(
                onClickRequestCancel = {
                    onRequestCancel()
                    scope.launch { sheetState.hide() }
                },
                onClickClose = {
                    scope.launch { sheetState.hide() }
                },
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
        LazyColumn(
            modifier = modifier
                .background(
                    color = Colors.White
                )
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxWidth(),
            state = scrollState
        ) {
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(vertical = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = member.profile.nickname,
                        style = Typography.T1.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray800
                    )

                    Spacer(Modifier.weight(1f))

                    Image(
                        modifier = Modifier
                            .clickableSingleWithNoRipple {
                                onAlarmClick()
                            },
                        painter = painterResource(Res.drawable.ic_alarm),
                        contentDescription = "alarm"
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Image(
                        modifier = Modifier
                            .clickableSingleWithNoRipple {
                                onSettingClick()
                            },
                        painter = painterResource(Res.drawable.ic_setting),
                        contentDescription = "setting"
                    )
                }

                PHorizontalDivider()
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 16.dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProfileImageView(
                            imgUrl = member.profile.profilePictureUrl,
                            size = 60.dp
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(start = 20.dp)
                                    .widthIn(min = 34.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = member.profile.reviewCount.toString(),
                                    color = Colors.Gray900,
                                    style = Typography.B2.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(top = 4.dp),
                                    text = Texts.Word.PINLOG,
                                    color = Colors.Gray400,
                                    style = Typography.B3.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            PVerticalDivider(
                                modifier = Modifier.height(22.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .widthIn(min = 34.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = member.profile.averageStarRating.toString(),
                                    color = Colors.Gray900,
                                    style = Typography.B2.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(top = 4.dp),
                                    text = Texts.PROFILE.AVERAGE_STAR_RATING,
                                    color = Colors.Gray400,
                                    style = Typography.B3.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            PVerticalDivider(
                                modifier = Modifier.height(22.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .padding(end = 20.dp)
                                    .widthIn(min = 34.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = member.profile.pinBuddyCount.toString(),
                                    color = Colors.Gray900,
                                    style = Typography.B2.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(top = 4.dp),
                                    text = Texts.Word.PIN_BUDDY,
                                    color = Colors.Gray400,
                                    style = Typography.B3.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = member.profile.nickname,
                        style = Typography.B2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray900,
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = member.profile.bio,
                        style = Typography.B3.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Colors.Gray400,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        RoundedBox(
                            modifier = Modifier
                                .weight(1f)
                                .clickableWithNoRipple {
                                    onClickShare()
                                },
                            cornerRounded = 8,
                            backgroundColor = Colors.Gray100,
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(vertical = 12.dp, horizontal = 16.dp)
                                    .align(Alignment.Center),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_share_profile),
                                    contentDescription = "share profile"
                                )

                                Text(
                                    modifier = Modifier
                                        .padding(start = 6.dp),
                                    text = Texts.PROFILE.SHARE_PROFILE,
                                    style = Typography.L1.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = Colors.Gray800,
                                )
                            }
                        }

                        Spacer(Modifier.width(10.dp))

                        when (member.relationType) {
                            RelationType.FRIEND -> AlreadyPinBuddyButton {
                                isShowDeleteDialog = true
                            }
                            RelationType.PENDING -> PendingButton {
                                onRequestCancel()
                            }
                            else -> StrangerButton {
                                onRequestPinBuddy()
                            }
                        }
                    }
                }
            }

            item {
                ContentView(
                    scope = scope,
                    pagerState = pagerState,
                    onClickPage = { pagerState.value = it }
                )
            }


            if (member.relationType != RelationType.FRIEND) {
                item {
                    LockReviewScreen()
                }
            } else {
                if (pagerState.value == 0) {
                    UserPinlogList(
                        reviewList = photoReviews,
                        onClickDetail = onClickDetail,
                        onClickLike = onClickLike,
                    )
                } else {
                    this.UserPinchList(
                        member = member,
                        pinchPageAble = pinchPageAble,
                        onClickGoCreatePinch = onMovePinchWrite,
                        onMoveDetail = onMoveDetail
                    )
                }
            }
        }
    }

    if (isShowDeleteDialog) {
        PDialog(
            titleText = Texts.PROFILE.REMOVE_PIN_BUDDY_DIALOG_TITLE,
            descriptionText = Texts.PROFILE.REMOVE_PIN_BUDDY_DIALOG_DESCRIPTION,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Word.DO_DELETE,
            onLeftButtonClick = {
                isShowDeleteDialog = false
            },
            onRightButtonClick = {
                onRemovePinBuddy()
                isShowDeleteDialog = false
            },
        )
    }
}

@Composable
private fun ContentView(
    scope: CoroutineScope,
    pagerState: MutableState<Int>,
    onClickPage: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .clickableWithNoRipple {
                    scope.launch {
                        onClickPage(0)
                    }
                }
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp, bottom = 11.dp),
                text = Texts.Word.PINLOG,
                style = if (pagerState.value == 0) Typography.B1.copy(fontWeight = FontWeight.SemiBold) else Typography.T2.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = if (pagerState.value == 0) Colors.Gray800 else Colors.Gray300,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(
                        color = if (pagerState.value == 0) Colors.Gray800 else Colors.Transparency
                    )
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clickableWithNoRipple {
                    scope.launch {
                        onClickPage(1)
                    }
                }
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 12.dp, bottom = 11.dp),
                text = Texts.Word.PINCH,
                style = if (pagerState.value == 1) Typography.B1.copy(fontWeight = FontWeight.SemiBold) else Typography.T2.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = if (pagerState.value == 1) Colors.Gray800 else Colors.Gray300,
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(
                        color = if (pagerState.value == 1) Colors.Gray800 else Colors.Transparency
                    )
            )
        }
    }

    PHorizontalDivider()
}

@Composable
private fun RowScope.StrangerButton(
    onRequestPinBuddy: () -> Unit = {},
) {
    RoundedBox(
        modifier = Modifier
            .weight(1f)
            .clickableSingleWithNoRipple {
                onRequestPinBuddy()
            },
        cornerRounded = 8,
        backgroundColor = Colors.Gray800,
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(14.dp),
                painter = painterResource(Res.drawable.ic_empty_receive_buddy),
                contentDescription = "add pinbuddy",
                colorFilter = ColorFilter.tint(Colors.Gray100)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = Texts.PROFILE.REQUEST_PIN_BUDDY,
                style = Typography.L1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray100,
            )
        }
    }
}

@Composable
private fun RowScope.PendingButton(
    onRequestCancel: () -> Unit = {},
) {
    RoundedBox(
        modifier = Modifier
            .weight(1f)
            .clickableSingleWithNoRipple {
                onRequestCancel()
            },
        cornerRounded = 8,
        backgroundColor = Colors.Gray100,
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(14.dp),
                painter = painterResource(Res.drawable.ic_empty_sent_buddy),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Colors.Gray800)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = Texts.PROFILE.ALREADY_REQUEST_PIN_BUDDY,
                style = Typography.L1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800,
            )
        }
    }
}

@Composable
private fun RowScope.AlreadyPinBuddyButton(
    onRemovePinBuddy: () -> Unit = {},
) {
    RoundedBox(
        modifier = Modifier
            .weight(1f)
            .clickableSingleWithNoRipple {
                onRemovePinBuddy()
            },
        cornerRounded = 8,
        backgroundColor = Colors.Gray100,
        cornerColor = Colors.Gray800
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier
                    .size(14.dp),
                painter = painterResource(Res.drawable.ic_empty_pin_buddy),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Colors.Gray800)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = Texts.Word.PIN_BUDDY,
                style = Typography.L1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = Colors.Gray800,
            )
        }
    }
}


@Composable
private fun LockReviewScreen() {
    Column(
        modifier = Modifier
            .background(color = Colors.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        Image(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            painter = painterResource(Res.drawable.ic_lock),
            contentDescription = "lock"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Colors.White),
            text = Texts.PROFILE.ROCK_PROFILE,
            color = Colors.Gray400,
            style = Typography.B1.copy(
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Center
        )
    }
}

fun LazyListScope.UserPinlogList(
    reviewList: List<Review>,
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
    onClickDetail: (Int) -> Unit = {},
) {
    if (reviewList.isEmpty()) {
        item {
            ReviewEmptyScreen()
        }
    } else {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(reviewList) {
            FeedView(
                item = it,
                onClickLike = onClickLike,
                onClickDetail = onClickDetail,
            )

            PHorizontalDivider()

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

fun LazyListScope.UserPinchList(
    member: Member,
    pinchPageAble: PintsPageAble,
    onClickGoCreatePinch: () -> Unit = {},
    onMoveDetail: (Int) -> Unit = {},
) {
    item {
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(start = 20.dp)
                .background(
                    color = Colors.Gray100,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .clickableWithNoRipple {
                    onClickGoCreatePinch()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Texts.Word.PINCH,
                color = Colors.Gray500,
                style = Typography.L1.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.width(5.dp))

            Image(
                modifier = Modifier
                    .height(6.dp),
                painter = painterResource(Res.drawable.ic_right_arrow_gray_400),
                colorFilter = ColorFilter.tint(Colors.Gray400),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }


    if (pinchPageAble.content.isEmpty()) {
        item {
            Spacer(modifier = Modifier.height(20.dp))

            RoundedBox(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                cornerColor = Colors.Gray200,
                cornerRounded = 12,
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 23.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Texts.PROFILE.EMPTY_PINCH(member.profile.name),
                        style = Typography.L2.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray400,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    RoundedBox(
                        backgroundColor = Colors.Main99,
                        cornerRounded = 5
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(horizontal = 18.dp, vertical = 4.dp)
                                .background(color = Colors.Main99)
                                .clickableWithNoRipple {
                                    onClickGoCreatePinch()
                                },
                            text = Texts.PROFILE.GO_PINCH_CREATE,
                            style = Typography.L3.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Colors.Main
                        )
                    }
                }
            }
        }
    } else {
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }

        items(pinchPageAble.content) {
            PinchItemView(
                pinchListItem = it,
                onMoveDetail = onMoveDetail
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ReviewEmptyScreen() {
    Column(
        modifier = Modifier
            .background(color = Colors.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(150.dp))

        Image(
            painter = painterResource(Res.drawable.ic_normal_face),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = Texts.PROFILE.EMPTY_USER_PINLOG,
            color = Colors.Gray400,
            style = Typography.B1.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )
    }
}