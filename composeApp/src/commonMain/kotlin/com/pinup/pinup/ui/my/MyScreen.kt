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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.FeedView
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PVerticalDivider
import com.pinup.pinup.ui.component.PinchItemView
import com.pinup.pinup.ui.component.PinlogMenuBottomSheet
import com.pinup.pinup.ui.component.ProfileImageView
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.ScrapItemView
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import kotlin.Boolean
import kotlin.Double
import kotlin.String

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
    onClickLike: (Int, Boolean) -> Unit = { _, _ -> },
    onClickShare: () -> Unit = {},
    onClickMoreScrap: () -> Unit = {},
    onMovePlaceDetail: (String) -> Unit = {},
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
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
                                pagerState.animateScrollToPage(0)
                            }
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp, bottom = 11.dp),
                        text = Texts.Word.PINLOG,
                        style = if (pagerState.currentPage == 0) Typography.B1.copy(fontWeight = FontWeight.SemiBold) else Typography.T2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = if (pagerState.currentPage == 0) Colors.Gray800 else Colors.Gray300,
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(
                                color = if (pagerState.currentPage == 0) Colors.Gray800 else Colors.Transparency
                            )
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickableWithNoRipple {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 12.dp, bottom = 11.dp),
                        text = Texts.PROFILE.PROFILE_MY,
                        style = if (pagerState.currentPage == 1) Typography.B1.copy(fontWeight = FontWeight.SemiBold) else Typography.T2.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = if (pagerState.currentPage == 1) Colors.Gray800 else Colors.Gray300,
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(
                                color = if (pagerState.currentPage == 1) Colors.Gray800 else Colors.Transparency
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
                    MyScrapList(
                        scrapList = emptyList(),
                        pinchListItem = emptyList(),
                        bottomBarHeight = bottomBarHeight,
                        onClickMoreScrap = onClickMoreScrap,
                        onClickPlaceDetail = onMovePlaceDetail
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
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
    onClickLike: (Int, Boolean) -> Unit = { _, _ -> },
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
            items(reviewList) {
                FeedView(
                    item = it,
                    onClickMenu = onClickMenu,
                    onClickLike = onClickLike,
                    onClickDetail = onClickDetail,
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

@Composable
fun MyScrapList(
    scrapList: List<ReviewedPlace>,
    pinchListItem: List<PinchListItem>,
    bottomBarHeight: Dp = 0.dp,
    onClickGoFeed: () -> Unit = {},
    onClickGoCreatePinch: () -> Unit = {},
    onClickPlaceDetail: (String) -> Unit = {},
    onClickMoreScrap: () -> Unit = {},
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(bottom = bottomBarHeight)
            .background(Colors.White),
        state = scrollState
    ) {
        item {
            Spacer(modifier = Modifier.height(19.dp))

            Row(
                modifier = Modifier
                    .clickableWithNoRipple {
                        onClickMoreScrap()
                    }
                    .background(
                        color = Colors.Gray100,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Texts.Word.SCRAP,
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

            if (scrapList.isEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))

                RoundedBox(
                    modifier = Modifier
                        .fillMaxWidth(),
                    cornerColor = Colors.Gray200,
                    cornerRounded = 12,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 15.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = Texts.PROFILE.EMPTY_SCRAP,
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
                                    .padding(horizontal = 19.dp, vertical = 4.dp)
                                    .background(color = Colors.Main99),
                                text = Texts.PROFILE.GO_PINLOG,
                                style = Typography.L3.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Main
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(15.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    if (scrapList.size >= 3) {
                        scrapList.take(3).forEach { scrap ->
                            ScrapItemView(
                                modifier = Modifier
                                    .clickableWithNoRipple {
                                        onClickPlaceDetail(scrap.kakaoPlaceId)
                                    }
                                    .weight(1f),
                                place = scrap
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    } else {
                        scrapList.forEach { scrap ->
                            ScrapItemView(
                                modifier = Modifier
                                    .clickableWithNoRipple {
                                        onClickPlaceDetail(scrap.kakaoPlaceId)
                                    }
                                    .width(105.dp),
                                place = scrap
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }

            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .background(
                        color = Colors.Gray100,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 6.dp),
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

            if (pinchListItem.isEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))

                RoundedBox(
                    modifier = Modifier
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
                            text = Texts.PROFILE.EMPTY_PINCH,
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
                                    .background(color = Colors.Main99),
                                text = Texts.PROFILE.GO_PINCH_CREATE,
                                style = Typography.L3.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Colors.Main
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(15.dp))

                this@LazyColumn.items(pinchListItem) {
                    PinchItemView(it)

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}