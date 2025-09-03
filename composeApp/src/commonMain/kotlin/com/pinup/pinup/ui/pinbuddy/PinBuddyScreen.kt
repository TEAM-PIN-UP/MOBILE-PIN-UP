package com.pinup.pinup.ui.pinbuddy

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.PinBuddyRequest
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.component.UserCard
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import com.pinup.pinup.ui.theme.Texts

@Composable
fun PinBuddyScreen(
    pinBuddies: PersistentList<Profile>,
    sentPinBuddyRequests: PersistentList<PinBuddyRequest>,
    receivePinBuddyRequests: PersistentList<PinBuddyRequest>,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
    onDeletePinBuddy: (Int) -> Unit = {},
    onDeletePinBuddyRequest: (Int) -> Unit = {},
    onAcceptClick: (Int) -> Unit = {},
    onRejectClick: (Int) -> Unit = {},
    onProfileClick: (Int) -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val isShowCompleteDialog = remember { mutableStateOf<Pair<Boolean, Int?>>(false to null) }
    val pages = remember { listOf(Texts.Word.PIN_BUDDY, Texts.PROFILE.RECEIVE_REQUEST , Texts.PROFILE.SENT_REQUEST) }
    val pagerState = rememberPagerState{ pages.size }

    Column(
        modifier = modifier
            .background(Colors.White)
            .fillMaxSize()
    ) {
        TitleBar(
            modifier = Modifier
                .padding(start = 20.dp),
            title = Texts.Word.PIN_BUDDY,
            onLeftButtonClick = {
                onBackPressed()
            }
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
                            text = item,
                            style = if (pagerState.currentPage == index) Typography.B1.copy(fontWeight = FontWeight.SemiBold)
                                    else Typography.T2.copy(fontWeight = FontWeight.Medium),
                            color = if (pagerState.currentPage == index) Colors.Gray800 else Colors.Gray300,
                            textAlign = TextAlign.Center
                        )

                        if (receivePinBuddyRequests.isNotEmpty()) {
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
            userScrollEnabled = false
        ) {
            when (it) {
                0 -> {
                    PinBuddyList(
                        pinBuddies = pinBuddies,
                        onProfileClick = onProfileClick,
                        onDeletePinBuddy = { memberId ->
                            isShowCompleteDialog.value = true to memberId
                        }
                    )
                }
                1 -> {
                    ReceivePinBuddyRequestList(
                        receivePinBuddyRequests = receivePinBuddyRequests,
                        onProfileClick = onProfileClick,
                        onAcceptClick = onAcceptClick,
                        onRejectClick = onRejectClick
                    )
                }
                else -> {
                    SentPinBuddyRequestList(
                        sentPinBuddyRequests = sentPinBuddyRequests,
                        onProfileClick = onProfileClick,
                        onDeletePinBuddyRequest = onDeletePinBuddyRequest
                    )
                }
            }
        }
    }

    if (isShowCompleteDialog.value.first) {
        PDialog(
            titleText = "핀버디를 삭제 하시겠어요?",
            descriptionText = "핀버디 삭제 시 재요청해야\n다시 핀버디를 맺을 수 있어요",
            leftButtonText = "취소",
            rightButtonText = "삭제",
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
    onProfileClick: (Int) -> Unit,
    onDeletePinBuddy: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "나의 핀버디",
                style = Typography.H3,
                color = Colors.Neutral800,
            )

            if (pinBuddies.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = pinBuddies.size.toString(),
                    style = Typography.H3,
                    color = Colors.Neutral400,
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(pinBuddies) {
                UserCard(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            onProfileClick(it.memberId)
                        },
                    imgUrl = it.profilePictureUrl,
                    nickname = it.nickname,
                    reviewCount = it.reviewCount,
                    pinBuddyCount = it.pinBuddyCount,
                    buttonContainer = {
                        RoundedBox(
                            modifier = Modifier
                                .clickableSingleWithNoRipple {
                                    onDeletePinBuddy(it.memberId)
                                },
                            cornerRounded = 6,
                            backgroundColor = Colors.Neutral100,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                text = "삭제",
                                color = Colors.Neutral800,
                                style = Typography.H6
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SentPinBuddyRequestList(
    sentPinBuddyRequests: PersistentList<PinBuddyRequest>,
    onProfileClick: (Int) -> Unit,
    onDeletePinBuddyRequest: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "보낸 신청",
                style = Typography.H3,
                color = Colors.Neutral800,
            )

            if (sentPinBuddyRequests.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = sentPinBuddyRequests.size.toString(),
                    style = Typography.H3,
                    color = Colors.Neutral400,
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(sentPinBuddyRequests) {
                UserCard(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            onProfileClick(it.receiver.memberId)
                        },
                    imgUrl = it.receiver.profilePictureUrl,
                    nickname = it.receiver.nickname,
                    reviewCount = it.receiver.reviewCount,
                    pinBuddyCount = it.receiver.pinBuddyCount,
                    buttonContainer = {
                        RoundedBox(
                            modifier = Modifier
                                .clickableSingleWithNoRipple {
                                    onDeletePinBuddyRequest(it.id)
                                },
                            cornerRounded = 6,
                            backgroundColor = Colors.Neutral100,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 8.dp, horizontal = 12.dp),
                                text = "신청 취소",
                                color = Colors.Neutral800,
                                style = Typography.H6
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ReceivePinBuddyRequestList(
    receivePinBuddyRequests: PersistentList<PinBuddyRequest>,
    onProfileClick: (Int) -> Unit,
    onRejectClick: (Int) -> Unit,
    onAcceptClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "받은 신청",
                style = Typography.H3,
                color = Colors.Neutral800,
            )

            if (receivePinBuddyRequests.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = receivePinBuddyRequests.size.toString(),
                    style = Typography.H3,
                    color = Colors.Neutral400,
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(receivePinBuddyRequests) {
                UserCard(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            onProfileClick(it.sender.memberId)
                        },
                    imgUrl = it.sender.profilePictureUrl,
                    nickname = it.sender.nickname,
                    reviewCount = it.sender.reviewCount,
                    pinBuddyCount = it.sender.pinBuddyCount,
                    buttonContainer = {
                        Row {
                            RoundedBox(
                                modifier = Modifier
                                    .clickableSingleWithNoRipple {
                                        onAcceptClick(it.id)
                                    },
                                cornerRounded = 6,
                                backgroundColor = Colors.Neutral100,
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 12.dp),
                                    text = "수락",
                                    color = Colors.Neutral800,
                                    style = Typography.H6
                                )
                            }
                            RoundedBox(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clickableSingleWithNoRipple {
                                        onRejectClick(it.id)
                                    },
                                cornerRounded = 6,
                                cornerColor = Colors.Neutral100,
                                backgroundColor = Colors.White
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 8.dp, horizontal = 12.dp),
                                    text = "거절",
                                    color = Colors.Neutral800,
                                    style = Typography.H6
                                )
                            }
                        }
                    }
                )
            }
        }
    }
}