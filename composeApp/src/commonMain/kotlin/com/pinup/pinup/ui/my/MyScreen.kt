package com.pinup.pinup.ui.my

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.FeedView
import com.pinup.pinup.ui.component.NotDevelopScreen
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PVerticalDivider
import com.pinup.pinup.ui.component.PinlogMenuBottomSheet
import com.pinup.pinup.ui.component.ProfileImageView
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun MyScreen(
    member: Member,
    reviews: List<Review>,
    onClickBottomNav: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
    onAlarmClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onAddPinBuddyClick: () -> Unit = {},
    onMovePinBuddy: () -> Unit = {},
    onClickEdit: (Int) -> Unit = {},
    onClickDelete: (Int) -> Unit = {},
    onClickPinLog: () -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
) {
    var index by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState { 2 }
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    var clickedReviewId by remember { mutableStateOf(0) }
    val density = LocalDensity.current
    var bottomBarHeight by remember { mutableStateOf(0.dp) }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            PinlogMenuBottomSheet(
                onClickEdit = {
                    onClickEdit(clickedReviewId)
                    scope.launch { sheetState.hide() }
                },
                onClickDelete = {
                    onClickDelete(clickedReviewId)
                    scope.launch { sheetState.hide() }
                },
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
        Column(
            modifier = modifier
                .background(
                    color = Colors.White
                )
                .statusBarsPadding()
                .fillMaxWidth()
        ) {
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
                                .widthIn(min = 34.dp)
                                .clickableSingleWithNoRipple {
                                    onMovePinBuddy()
                                },
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
                            .weight(1f),
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

                    RoundedBox(
                        modifier = Modifier
                            .weight(1f)
                            .clickableSingleWithNoRipple {
                                onAddPinBuddyClick()
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
                                painter = painterResource(Res.drawable.ic_add_pinbuddy),
                                contentDescription = "add pinbuddy"
                            )

                            Text(
                                modifier = Modifier
                                    .padding(start = 6.dp),
                                text = Texts.PROFILE.ADD_PIN_BUDDY,
                                style = Typography.L1.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = Colors.Gray800,
                            )
                        }
                    }
                }
            }

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
                                index = 0
                                pagerState.animateScrollToPage(index)
                            }
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp, bottom = 11.dp),
                        text = Texts.Word.PINLOG,
                        style = if (index == 0) Typography.B1.copy(fontWeight = FontWeight.SemiBold) else Typography.T2.copy(fontWeight = FontWeight.Medium),
                        color = if (index == 0) Colors.Gray800 else Colors.Gray300,
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(
                                color = if (index == 0) Colors.Gray800 else Colors.Transparency
                            )
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickableWithNoRipple {
                            scope.launch {
                                index = 1
                                pagerState.animateScrollToPage(index)
                            }
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp, bottom = 11.dp),
                        text = Texts.Word.SCRAP,
                        style = if (index == 1) Typography.B1.copy(fontWeight = FontWeight.SemiBold) else Typography.T2.copy(fontWeight = FontWeight.Medium),
                        color = if (index == 1) Colors.Gray800 else Colors.Gray300,
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(
                                color = if (index == 1) Colors.Gray800 else Colors.Transparency
                            )
                    )
                }
            }

            PHorizontalDivider()

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) {
                if (it == 0) {
                    MyPinLogList(
                        bottomBarHeight = bottomBarHeight,
                        reviewList = reviews,
                        onClickMenu = {
                            clickedReviewId = it
                            scope.launch { sheetState.show() }
                        },
                        onClickPinLog = onClickPinLog,
                        onClickLike = onClickLike,
                        onClickDetail = onClickDetail
                    )
                } else {
                    NotDevelopScreen {

                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            Box(
                modifier = Modifier
                    .onGloballyPositioned { coords ->
                        bottomBarHeight = with(density) { coords.size.height.toDp() }
                    }
            ) {
                BottomBar(
                    selectedMenu = MainDestination.My,
                    profileImage = member.profile.profilePictureUrl ?: "",
                    onBottomMenuClick = onClickBottomNav
                )
            }
        }
    }
}

@Composable
fun MyPinLogList(
    reviewList: List<Review>,
    bottomBarHeight: Dp = 0.dp,
    onClickMenu: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
    onClickDetail: (Int) -> Unit = {},
    onClickPinLog: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()
    if (reviewList.isEmpty()) {
        ReviewEmptyScreen(
            onClickPinLog = onClickPinLog
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp, bottom = bottomBarHeight)
                .background(Colors.White),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            state = scrollState
        ) {
            items(reviewList){
                FeedView(
                    item = it,
                    onClickMenu = onClickMenu,
                    onClickLike = onClickLike,
                    onClickDetail = onClickDetail,
                    onClickScrap = {},
                )

                PHorizontalDivider()
            }
        }
    }
}

@Composable
private fun ReviewEmptyScreen(
    onClickPinLog: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(color = Colors.White)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(116.dp))

        Text(
            text = Texts.PROFILE.EMPTY_MY_PINLOG,
            color = Colors.Gray400,
            style = Typography.B1.copy(
                fontWeight = FontWeight.Medium
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .clickableWithNoRipple {
                    onClickPinLog()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Texts.PROFILE.WRITE_FIRST_PINLOG,
                color = Colors.Gray600,
                style = Typography.B2.copy(
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(4.dp))

            Image(
                modifier = Modifier
                    .size(14.dp),
                painter = painterResource(Res.drawable.ic_right_arrow),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Colors.Gray600)
            )
        }
    }
}