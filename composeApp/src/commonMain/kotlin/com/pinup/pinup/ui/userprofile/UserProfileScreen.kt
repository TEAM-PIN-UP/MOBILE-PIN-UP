package com.pinup.pinup.ui.userprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.RelationType
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PVerticalDivider
import com.pinup.pinup.ui.component.ProfileImageView
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.TitleBar
import com.pinup.pinup.ui.my.PhotoReviewList
import com.pinup.pinup.ui.my.TextReviewList
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun UserProfileScreen(
    member: Member,
    photoReviews: List<Review>,
    textReviews: List<Review>,
    modifier: Modifier = Modifier,
    onRequestPinBuddy: (Int) -> Unit = {},
    onRequestCancel: (Int) -> Unit = {},
    onRemovePinBuddy: (Int) -> Unit = {},
    onBackPressed: () -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val pages = remember { listOf("포토 리뷰","텍스트 리뷰") }
    val pagerState = rememberPagerState{ pages.size }
    val isShowRequestDialog = remember { mutableStateOf<Pair<Boolean, Int?>>(false to null) }
    val isShowDeleteDialog = remember { mutableStateOf<Pair<Boolean, Int?>>(false to null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Colors.White),
    ) {
        TitleBar(
            title = member.profile.nickname,
            onLeftButtonClick = onBackPressed
        )

        PHorizontalDivider()

        Column(
            modifier = Modifier
                .padding(top = 20.dp, bottom = 16.dp)
                .padding(horizontal = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImageView(
                    imgUrl = member.profile.profilePictureUrl,
                    size = 56.dp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(min = 44.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = member.profile.reviewCount.toString(),
                            color = Colors.Neutral800,
                            style = Typography.H4
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            text = "리뷰",
                            color = Colors.Neutral500,
                            style = Typography.B4
                        )
                    }

                    PVerticalDivider(
                        modifier = Modifier.height(24.dp)
                    )
                    Column(
                        modifier = Modifier
                            .widthIn(min = 44.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = member.profile.averageStarRating.toString(),
                            color = Colors.Neutral800,
                            style = Typography.H4
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            text = "평균 평점",
                            color = Colors.Neutral500,
                            style = Typography.B4
                        )
                    }

                    PVerticalDivider(
                        modifier = Modifier.height(24.dp)
                    )
                    Column(
                        modifier = Modifier
                            .widthIn(min = 44.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = member.profile.pinBuddyCount.toString(),
                            color = Colors.Neutral800,
                            style = Typography.H4
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            text = "핀버디",
                            color = Colors.Neutral500,
                            style = Typography.B4
                        )
                    }
                }
            }

            Text(
                modifier = Modifier
                    .padding(top = 16.dp),
                text = member.profile.nickname,
                style = Typography.H4,
                color = Colors.Neutral800,
            )

            if (member.profile.bio.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp),
                    text = member.profile.bio,
                    style = Typography.B4,
                    color = Colors.Neutral600,
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
            ) {
                RoundedBox(
                    modifier = Modifier
                        .weight(1f),
                    cornerRounded = 8,
                    backgroundColor = Colors.Neutral100,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
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
                            text = "프로필 공유",
                            style = Typography.H4,
                            color = Colors.Neutral800,
                        )
                    }
                }

                Spacer(Modifier.width(11.dp))

                RoundedBox(
                    modifier = Modifier
                        .weight(1f)
                        .clickableSingleWithNoRipple {
                            when (member.relationType) {
                                RelationType.FRIEND -> {
                                    isShowDeleteDialog.value = true to member.profile.memberId
                                }

                                RelationType.PENDING -> {
                                    member.friendRequestId?.let(onRequestCancel)
                                }

                                else -> {
                                    isShowRequestDialog.value = true to member.profile.memberId
                                }
                            }
                        },
                    cornerRounded = 8,
                    backgroundColor = Colors.Neutral100,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 12.dp)
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
                            text = when(member.relationType) {
                                RelationType.FRIEND -> "핀버디 취소"
                                RelationType.PENDING -> "신청 취소"
                                else -> "핀버디 신청"
                            },
                            style = Typography.H4,
                            color = Colors.Neutral800,
                        )
                    }
                }
            }
        }

        LazyRow(
            modifier = Modifier
                .padding(top = 8.dp, start = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(pages) { index, item ->
                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .clickableWithNoRipple {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                ) {
                    Text(
                        modifier = Modifier
                            .width(IntrinsicSize.Max)
                            .padding(top = 12.dp, bottom = 11.dp),
                        text = if (index == 0) {
                            if (photoReviews.isNotEmpty()) {
                                "$item ${photoReviews.size}"
                            } else {
                                item
                            }
                        } else {
                            if (textReviews.isNotEmpty()) {
                                "$item ${textReviews.size}"
                            } else {
                                item
                            }
                        },
                        style = Typography.H3,
                        color = if (pagerState.currentPage == index) Colors.Neutral800 else Colors.Neutral300,
                        textAlign = TextAlign.Center
                    )

                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .fillMaxWidth()
                            .background(
                                color = if (pagerState.currentPage == index) Colors.Neutral800 else Colors.Transparency
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
            if (member.relationType != RelationType.FRIEND) {
                LockReviewScreen()
            } else {
                if (it == 0) {
                    PhotoReviewList(
                        photoReviews = photoReviews
                    )
                } else {
                    TextReviewList(
                        textReviews = textReviews
                    )
                }
            }
        }
    }


    if (isShowRequestDialog.value.first) {
        PDialog(
            titleText = "핀버디를 신청 하시겠어요?",
            descriptionText = "상대가 신청을 수락해야\n핀버디가 맺어져요.",
            leftButtonText = "취소",
            rightButtonText = "신청",
            onLeftButtonClick = {
                isShowRequestDialog.value = false to null
            },
            onRightButtonClick = {
                isShowRequestDialog.value.second?.let {
                    onRequestPinBuddy(it)
                }
                isShowRequestDialog.value = false to null
            },
        )
    }

    if (isShowDeleteDialog.value.first) {
        PDialog(
            titleText = "핀버디를 삭제 하시겠어요?",
            descriptionText = "핀버디 삭제 시 재요청해야\n다시 핀버디를 맺을 수 있어요",
            leftButtonText = "취소",
            rightButtonText = "삭제",
            onLeftButtonClick = {
                isShowDeleteDialog.value = false to null
            },
            onRightButtonClick = {
                isShowDeleteDialog.value.second?.let {
                    onRemovePinBuddy(it)
                }
                isShowDeleteDialog.value = false to null
            },
        )
    }
}

@Composable
private fun LockReviewScreen() {
    Column {
        Image(
            modifier = Modifier
                .padding(top = 100.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(Res.drawable.ic_lock),
            contentDescription = "lock"
        )

        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .background(color = Colors.White),
            text = "게시물을 보려면 핀버디를\n신청하세요.",
            color = Colors.Neutral800,
            style = Typography.H4,
            textAlign = TextAlign.Center
        )
    }
}