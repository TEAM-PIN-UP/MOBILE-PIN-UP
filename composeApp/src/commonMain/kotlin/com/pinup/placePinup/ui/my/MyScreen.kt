package com.pinup.placePinup.ui.my

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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.Member
import com.pinup.placePinup.domain.model.PintsPageAble
import com.pinup.placePinup.domain.model.Review
import com.pinup.placePinup.extentions.ScrollToEndCallback
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.BottomBar
import com.pinup.placePinup.ui.component.FeedView
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.PVerticalDivider
import com.pinup.placePinup.ui.component.PinchItemView
import com.pinup.placePinup.ui.component.PinlogMenuBottomSheet
import com.pinup.placePinup.ui.component.ProfileImageView
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.component.ScrapItemView
import com.pinup.placePinup.ui.main.compose.MainDestination
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import kotlin.Boolean
import kotlin.String

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MyScreen(
    member: Member,
    reviews: List<Review>,
    scrapList: List<BookmarkedPlace>,
    pinchPageAble: PintsPageAble,
    onClickBottomNav: (MainDestination) -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onAlarmClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onMovePinBuddy: () -> Unit = {},
    onClickEdit: (Int) -> Unit = {},
    onClickDelete: (Int) -> Unit = {},
    onClickPinLog: () -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = { _, _ -> },
    onClickShare: () -> Unit = {},
    onClickMoreScrap: () -> Unit = {},
    onMovePlaceDetail: (String) -> Unit = {},
    onMovePinchWrite: () -> Unit = {},
    getMorePints: () -> Unit = {},
    onMoveDetail: (Int) -> Unit = {},
    onRefresh: () -> Unit = {},
    onProfileModifyClick: () -> Unit = {},
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    var clickedReviewId by remember { mutableStateOf(0) }
    var bottomBarHeight by remember { mutableStateOf(0.dp) }
    val pagerState = rememberSaveable { mutableStateOf(0) }
    val scrollState = rememberLazyListState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    ScrollToEndCallback(scrollState) {
        if (pagerState.value == 1 && !pinchPageAble.last) {
            getMorePints()
        }
    }

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
        Box(
            Modifier
                .statusBarsPadding()
                .pullRefresh(state = pullRefreshState,)
        ) {
            LazyColumn(
                modifier = modifier
                    .background(
                        color = Colors.White
                    )
                    .padding(bottom = bottomBarHeight)
                    .statusBarsPadding()
                    .fillMaxSize(),
                state = scrollState
            ) {
                item {
                    HeaderItem(
                        member = member,
                        onAlarmClick = onAlarmClick,
                        onSettingClick = onSettingClick
                    )
                }

                item {
                    ProfileItem(
                        member = member,
                        onMovePinBuddy = onMovePinBuddy,
                        onClickShare = onClickShare,
                        onProfileModifyClick = onProfileModifyClick
                    )
                }

                item {
                    ContentView(
                        scope = scope,
                        pagerState = pagerState,
                        onClickPage = { pagerState.value = it }
                    )
                }

                if (pagerState.value == 0) {
                    this.MyPinLogList(
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
                    this.MyScrapList(
                        member = member,
                        scrapList = scrapList,
                        pinchPageAble = pinchPageAble,
                        onClickMoreScrap = onClickMoreScrap,
                        onClickPlaceDetail = onMovePlaceDetail,
                        onClickGoCreatePinch = onMovePinchWrite,
                        onClickGoFeed = { onClickBottomNav(MainDestination.Feed) },
                        onMoveDetail = onMoveDetail
                    )
                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar(
                selectedMenu = MainDestination.My,
                profileImage = member.profile.profilePictureUrl ?: "",
                onBottomMenuClick = onClickBottomNav,
                onSizeChanged = { bottomBarHeight = it }
            )
        }
    }
}

@Composable
private fun HeaderItem(
    member: Member,
    onAlarmClick: () -> Unit,
    onSettingClick: () -> Unit
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
}

@Composable
private fun ProfileItem(
    member: Member,
    onMovePinBuddy: () -> Unit,
    onClickShare: () -> Unit,
    onProfileModifyClick: () -> Unit,
) {
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
                        onProfileModifyClick()
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
                        text = Texts.Setting.PROFILE_SETTING,
                        style = Typography.L1.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray800,
                    )
                }
            }
        }
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
                text = Texts.PROFILE.PROFILE_MY,
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

fun LazyListScope.MyPinLogList(
    reviewList: List<Review>,
    onClickMenu: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = { _, _ -> },
    onClickDetail: (Int) -> Unit = {},
    onClickPinLog: () -> Unit = {},
) {
    if (reviewList.isEmpty()) {
        item {
            ReviewEmptyScreen(
                onClickPinLog = onClickPinLog
            )
        }
    } else {
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(reviewList) {
            FeedView(
                item = it,
                onClickMenu = onClickMenu,
                onClickLike = onClickLike,
                onClickDetail = onClickDetail,
            )

            PHorizontalDivider()

            Spacer(modifier = Modifier.height(10.dp))
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

fun LazyListScope.MyScrapList(
    member: Member,
    scrapList: List<BookmarkedPlace>,
    pinchPageAble: PintsPageAble,
    onClickGoFeed: () -> Unit = {},
    onClickGoCreatePinch: () -> Unit = {},
    onClickPlaceDetail: (String) -> Unit = {},
    onClickMoreScrap: () -> Unit = {},
    onMoveDetail: (Int) -> Unit = {},
) {
    item {
        Spacer(modifier = Modifier.height(19.dp))

        Row(
            modifier = Modifier
                .padding(start = 20.dp)
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
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth(),
                cornerColor = Colors.Gray200,
                cornerRounded = 12,
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 14.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = Texts.PROFILE.EMPTY_SCRAP,
                        style = Typography.B3.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray400,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    Row(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onClickGoFeed()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Texts.PROFILE.GO_PINLOG,
                            style = Typography.L1.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Main
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Image(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(Res.drawable.ic_right_arrow),
                            colorFilter = ColorFilter.tint(Colors.Main),
                            contentDescription = null
                        )
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
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
                        style = Typography.B3.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray400,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(11.dp))

                    Row(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onClickGoCreatePinch()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Texts.PROFILE.GO_PINCH_CREATE,
                            style = Typography.L1.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Main
                        )

                        Spacer(modifier = Modifier.width(2.dp))

                        Image(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(Res.drawable.ic_right_arrow),
                            colorFilter = ColorFilter.tint(Colors.Main),
                            contentDescription = null
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