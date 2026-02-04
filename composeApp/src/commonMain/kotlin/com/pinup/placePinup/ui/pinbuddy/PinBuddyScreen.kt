package com.pinup.placePinup.ui.pinbuddy

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.PinBuddyRequest
import com.pinup.placePinup.domain.model.Profile
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.component.UserCard
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.pinup.placePinup.domain.model.PagingPinBuddy
import com.pinup.placePinup.domain.model.PagingPinBuddyRequest
import com.pinup.placePinup.extentions.ScrollToEndCallback
import com.pinup.placePinup.ui.theme.Texts
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PinBuddyScreen(
    pinBuddies: PagingPinBuddy,
    sentPinBuddyRequests: PagingPinBuddyRequest,
    receivePinBuddyRequests: PagingPinBuddyRequest,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onDeletePinBuddy: (Int) -> Unit = {},
    onDeletePinBuddyRequest: (Int) -> Unit = {},
    onAcceptClick: (Int) -> Unit = {},
    onRejectClick: (Int) -> Unit = {},
    onProfileClick: (Int, Int) -> Unit = {_, _ -> },
    onClickSearch: () -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope(),
    onRefresh: () -> Unit = {},
    isRefreshing: Boolean = false,
    getMorePinBuddy: () -> Unit,
    getMoreReceivePinBuddy: () -> Unit,
    getMoreSentPinBuddy: () -> Unit,
) {
    val isShowCompleteDialog = remember { mutableStateOf<Pair<Boolean, Int?>>(false to null) }
    val pages = remember { listOf(Texts.Word.PIN_BUDDY, Texts.PROFILE.RECEIVE_REQUEST , Texts.PROFILE.SENT_REQUEST) }
    val listSize = listOf(pinBuddies.totalElements, receivePinBuddyRequests.totalElements, sentPinBuddyRequests.totalElements)
    val pagerState = rememberPagerState{ pages.size }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh
    )

    Box(
        Modifier
            .background(Colors.White)
            .statusBarsPadding()
            .pullRefresh(
                state = pullRefreshState,
            )
    ) {
        Column(
            modifier = modifier
                .background(Colors.White)
                .fillMaxSize()
        ) {
            TitleBar(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                title = Texts.Word.PIN_BUDDY,
                onLeftButtonClick = onBackPressed,
                rightIcon = painterResource(Res.drawable.ic_search),
                onRightButtonClick = onClickSearch
            )

            PHorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                pages.forEachIndexed{ index, item ->
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickableWithNoRipple {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 12.dp, bottom = 11.dp),
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "$item ${listSize[index]}",
                                style = if (pagerState.currentPage == index) Typography.B1.copy(fontWeight = FontWeight.SemiBold)
                                else Typography.T2.copy(fontWeight = FontWeight.Medium),
                                color = if (pagerState.currentPage == index) Colors.Gray800 else Colors.Gray300,
                                textAlign = TextAlign.Center
                            )

                            if (receivePinBuddyRequests.pinBuddyRequests.isNotEmpty()) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_new_alarm),
                                    contentDescription = "new pinBuddy request"
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(
                                    color = if (pagerState.currentPage == index) Colors.Gray800 else Colors.Transparency
                                )
                        )
                    }
                }
            }

            PHorizontalDivider()

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                verticalAlignment = Alignment.Top
            ) {
                when (it) {
                    0 -> {
                        PinBuddyList(
                            pinBuddies = pinBuddies.profiles.toPersistentList(),
                            onProfileClick = onProfileClick,
                            onDeletePinBuddy = { memberId ->
                                isShowCompleteDialog.value = true to memberId
                            },
                            getMorePinBuddy = getMorePinBuddy
                        )
                    }
                    1 -> {
                        ReceivePinBuddyRequestList(
                            receivePinBuddyRequests = receivePinBuddyRequests.pinBuddyRequests.toPersistentList(),
                            onProfileClick = onProfileClick,
                            onAcceptClick = onAcceptClick,
                            onRejectClick = onRejectClick,
                            getMoreReceivePinBuddy = getMoreReceivePinBuddy
                        )
                    }
                    else -> {
                        SentPinBuddyRequestList(
                            sentPinBuddyRequests = sentPinBuddyRequests.pinBuddyRequests.toPersistentList(),
                            onProfileClick = onProfileClick,
                            onDeletePinBuddyRequest = onDeletePinBuddyRequest,
                            getMoreSentPinBuddy = getMoreSentPinBuddy
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }

    if (isShowCompleteDialog.value.first) {
        PDialog(
            titleText = Texts.PROFILE.DIALOG_DELETE_PIN_BUDDY_TITLE,
            descriptionText = Texts.PROFILE.DIALOG_DELETE_PIN_BUDDY_DESCRIPTION,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Word.DO_DELETE,
            onLeftButtonClick = {
                isShowCompleteDialog.value = false to null
            },
            onRightButtonClick = {
                isShowCompleteDialog.value.second?.let {
                    onDeletePinBuddy(it)
                }
                isShowCompleteDialog.value = false to null
            },
        )
    }
}

@Composable
private fun PinBuddyList(
    pinBuddies: PersistentList<Profile>,
    onProfileClick: (Int, Int) -> Unit,
    onDeletePinBuddy: (Int) -> Unit,
    getMorePinBuddy: () -> Unit,
) {
    val scrollState = rememberLazyListState()

    ScrollToEndCallback(scrollState) {
        getMorePinBuddy()
    }

    if (pinBuddies.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_empty_pin_buddy),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Texts.PROFILE.EMPTY_PIN_BUDDY,
                color = Colors.Gray400,
                style = Typography.B1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(pinBuddies) {
                UserCard(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            onProfileClick(it.memberId, -1)
                        },
                    imgUrl = it.profilePictureUrl,
                    nickname = it.nickname,
                    buttonContainer = {
                        RoundedBox(
                            modifier = Modifier
                                .clickableSingleWithNoRipple {
                                    onDeletePinBuddy(it.memberId)
                                },
                            cornerRounded = 8,
                            backgroundColor = Colors.Gray200,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                text = Texts.Word.DELETE,
                                color = Colors.Gray500,
                                style = Typography.L1.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                )
            }

            item {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun SentPinBuddyRequestList(
    sentPinBuddyRequests: PersistentList<PinBuddyRequest>,
    onProfileClick: (Int, Int) -> Unit,
    onDeletePinBuddyRequest: (Int) -> Unit,
    getMoreSentPinBuddy: () -> Unit,
) {
    val scrollState = rememberLazyListState()

    ScrollToEndCallback(scrollState) {
        getMoreSentPinBuddy()
    }

    if (sentPinBuddyRequests.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_empty_sent_buddy),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Texts.PROFILE.EMPTY_SENT_PIN_BUDDY,
                color = Colors.Gray400,
                style = Typography.B1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(sentPinBuddyRequests) {
                UserCard(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            onProfileClick(it.receiver.memberId, -1)
                        },
                    imgUrl = it.receiver.profilePictureUrl,
                    nickname = it.receiver.nickname,
                    buttonContainer = {
                        RoundedBox(
                            modifier = Modifier
                                .clickableSingleWithNoRipple {
                                    onDeletePinBuddyRequest(it.id)
                                },
                            cornerRounded = 8,
                            backgroundColor = Colors.Gray200,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                text = Texts.PROFILE.CANCEL_SENT_REQUEST,
                                color = Colors.Gray500,
                                style = Typography.L1.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                )
            }

            item {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}

@Composable
private fun ReceivePinBuddyRequestList(
    receivePinBuddyRequests: PersistentList<PinBuddyRequest>,
    onProfileClick: (Int, Int) -> Unit,
    onRejectClick: (Int) -> Unit,
    onAcceptClick: (Int) -> Unit,
    getMoreReceivePinBuddy: () -> Unit,
) {
    val scrollState = rememberLazyListState()

    ScrollToEndCallback(scrollState) {
        getMoreReceivePinBuddy()
    }

    if (receivePinBuddyRequests.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(Res.drawable.ic_empty_receive_buddy),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = Texts.PROFILE.EMPTY_RECEIVE_PIN_BUDDY,
                color = Colors.Gray400,
                style = Typography.B1.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(receivePinBuddyRequests) {
                UserCard(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            onProfileClick(it.sender.memberId, it.id)
                        },
                    imgUrl = it.sender.profilePictureUrl,
                    nickname = it.sender.nickname,
                    buttonContainer = {
                        Row {
                            RoundedBox(
                                modifier = Modifier
                                    .clickableSingleWithNoRipple {
                                        onAcceptClick(it.id)
                                    },
                                cornerRounded = 8,
                                backgroundColor = Colors.Gray800,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 12.dp),
                                    text = Texts.Word.ACCEPT,
                                    color = Colors.Gray100,
                                    style = Typography.L1.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            RoundedBox(
                                modifier = Modifier
                                    .clickableSingleWithNoRipple {
                                        onRejectClick(it.id)
                                    },
                                cornerRounded = 6,
                                cornerColor = Colors.Gray200,
                                backgroundColor = Colors.Gray200
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 12.dp),
                                    text = Texts.Word.REFUSE,
                                    color = Colors.Gray500,
                                    style = Typography.L1.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                )
            }

            item {
                Spacer(Modifier.navigationBarsPadding())
            }
        }
    }
}