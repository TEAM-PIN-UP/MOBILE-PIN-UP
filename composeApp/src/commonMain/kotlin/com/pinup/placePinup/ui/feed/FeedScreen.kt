package com.pinup.placePinup.ui.feed
import org.jetbrains.compose.resources.stringResource

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.pinup.placePinup.domain.model.Review
import com.pinup.placePinup.extentions.ScrollToEndCallback
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.BottomBar
import com.pinup.placePinup.ui.component.FeedView
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.PinlogMenuBottomSheet
import com.pinup.placePinup.ui.main.compose.MainDestination
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_search

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FeedScreen(
    reviewList: List<Review>,
    profile: String,
    isRefreshing: Boolean,
    getMoreFeed: () -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    onClickSearch: () -> Unit,
    onClickEdit: (Int) -> Unit = {},
    onClickDelete: (Int) -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = { _, _ -> },
    onRefresh: () -> Unit = {},
    onMoveUserProfile: (String) -> Unit = {},
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    var clickedReviewId by remember { mutableStateOf(0) }
    val scrollState = rememberLazyListState()
    var bottomBarHeight by remember { mutableStateOf(0.dp) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    ScrollToEndCallback(scrollState) {
        getMoreFeed()
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Colors.White
                )
        ) {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.word_feed),
                    style = Typography.T1.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray800
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onClickSearch()
                        },
                    painter = painterResource(Res.drawable.ic_search),
                    contentDescription = null,
                )
            }

            PHorizontalDivider()

            if (reviewList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.feed_empty_feed),
                        color = Colors.Gray400,
                        style = Typography.B1.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            } else {
                Box(
                    Modifier
                        .weight(1f)
                        .pullRefresh(
                            state = pullRefreshState,
                        )
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(bottom = bottomBarHeight)
                            .background(Colors.White),
                        state = scrollState,
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .height(16.dp)
                            )
                        }

                        items(reviewList) {
                            FeedView(
                                item = it,
                                onClickMenu = {
                                    clickedReviewId = it
                                    scope.launch { sheetState.show() }
                                },
                                onClickLike = onClickLike,
                                onClickDetail = onClickDetail,
                                onMoveUserProfile = onMoveUserProfile
                            )

                            PHorizontalDivider(modifier = Modifier.height(10.dp))

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .zIndex(1f)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar(
                selectedMenu = MainDestination.Feed,
                profileImage = profile,
                onBottomMenuClick = { destination ->
                    if (destination == MainDestination.Feed) {
                        // 이미 피드 화면일 때 피드 메뉴를 다시 누르면 맨 위로 이동
                        scope.launch { scrollState.animateScrollToItem(0) }
                    } else {
                        onClickBottomNav(destination)
                    }
                },
                onSizeChanged = { bottomBarHeight = it }
            )
        }
    }
}