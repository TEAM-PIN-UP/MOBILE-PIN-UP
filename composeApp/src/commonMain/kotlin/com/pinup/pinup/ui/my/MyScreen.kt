package com.pinup.pinup.ui.my

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.Review
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.PVerticalDivider
import com.pinup.pinup.ui.component.ProfileImageView
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.component.TextReview
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun MyScreen(
    member: Member,
    photoReviews: List<Review>,
    textReviews: List<Review>,
    modifier: Modifier = Modifier,
    onAlarmClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onAddPinBuddyClick: () -> Unit = {},
    onMovePinBuddy: () -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val pages = remember { listOf("포토 리뷰","텍스트 리뷰") }
    val pagerState = rememberPagerState{ pages.size }

    Column(
        modifier = modifier
            .background(
                color = Colors.White
            )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My",
                style = Typography.H2,
                color = Colors.Neutral800
            )

            Spacer(Modifier.weight(1f))

            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clickableSingleWithNoRipple {
                        onAlarmClick()
                    },
                painter = painterResource(Res.drawable.ic_alarm),
                contentDescription = "alarm"
            )

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
                            .widthIn(min = 44.dp)
                            .clickableSingleWithNoRipple {
                                onMovePinBuddy()
                            },
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

            if (member.profile.bio.isBlank()) {
                RoundedBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    cornerColor = Colors.Neutral100,
                    cornerRounded = 8,
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .padding(vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_my_description),
                            contentDescription = "my description"
                        )

                        Text(
                            modifier = Modifier
                                .padding(start = 10.dp),
                            text = "나를 소개해보세요",
                            style = Typography.B4,
                            color = Colors.Neutral400,
                        )
                    }
                }
            } else {
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
                            onAddPinBuddyClick()
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
                            text = "핀버디 추가",
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

@Composable
fun PhotoReviewList(
    photoReviews: List<Review>
) {
    val lazyGridState = rememberLazyGridState()
    if (photoReviews.isEmpty()) {
        ReviewEmptyScreen()
    } else {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            state = lazyGridState,
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(1.5.dp),
            verticalArrangement = Arrangement.spacedBy(1.5.dp)
        ) {
            items(photoReviews) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(Colors.Neutral200),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = it.reviewImageUrls?.first(),
                        contentDescription = "review image"
                    )
                }
            }
        }
    }
}

@Composable
fun TextReviewList(
    textReviews: List<Review>
) {
    val lazyGridState = rememberLazyListState()
    if (textReviews.isEmpty()) {
        ReviewEmptyScreen()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Neutral100),
            state = lazyGridState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(textReviews) {
                TextReview(
                    review = it,
                    onAdminClick = {},
                    onDetailClick = {}
                )
            }
        }
    }
}

@Composable
fun ReviewEmptyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier
                .padding(top = 100.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(Res.drawable.ic_empty_review),
            contentDescription = "empty"
        )
        Text(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .background(color = Colors.White),
            text = "아직 작성한 리뷰가 없어요!",
            color = Colors.Neutral800,
            style = Typography.H4,
            textAlign = TextAlign.Center
        )
    }
}