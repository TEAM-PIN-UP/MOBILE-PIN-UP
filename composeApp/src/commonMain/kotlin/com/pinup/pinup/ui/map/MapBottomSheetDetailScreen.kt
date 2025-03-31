package com.pinup.pinup.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
//import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RatingGraphView
import com.pinup.pinup.ui.component.ReviewCard
import com.pinup.pinup.ui.component.ReviewedPlaceCard
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapBottomSheetDetailScreen(
    detailPlace: DetailPlace?,
    isExpanded: Boolean,
    onMoveWriteReview: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    onClearDetailPlace: () -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
) {
    val scrollState = rememberLazyListState()
    var dragOffset by remember {
        mutableFloatStateOf(0f)
    }
    val isDragUp by remember {
        derivedStateOf {
            dragOffset <= 0
        }
    }
    val firstItemVisible by remember {
        derivedStateOf {
            scrollState.firstVisibleItemScrollOffset == 0 && scrollState.firstVisibleItemIndex == 0
        }
    }
    var userScrollable by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(firstItemVisible, isExpanded, isDragUp) {
        userScrollable = isExpanded && (firstItemVisible.not() || isDragUp)
    }

//    BackHandler {
//        onClearDetailPlace()
//        onBackPressed()
//    }

    if (detailPlace == null) {

    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Colors.White
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clickableWithNoRipple {
                            onClearDetailPlace()
                            onBackPressed()
                        },
                    painter = painterResource(Res.drawable.ic_bottomsheet_back),
                    contentDescription = "bottomsheet back"
                )

                RoundedBox(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .clickableSingleWithNoRipple {
                            onUpdateBookmark(
                                detailPlace.mapPlace.kakaoPlaceId,
                                detailPlace.mapPlace.bookmark
                            )
                        },
                    backgroundColor = Colors.Neutral50,
                    cornerRounded = 100
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(10.dp),
                        painter = if (detailPlace.mapPlace.bookmark) {
                            painterResource(Res.drawable.ic_bookmark_on)
                        } else {
                            painterResource(Res.drawable.ic_bookmark_off)
                        },
                        contentDescription = "boomark"
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull()
                                val dragAmount = change?.positionChange()?.y ?: 0f
                                dragOffset = dragAmount
                            }
                        }
                    },
                userScrollEnabled = userScrollable,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                state = scrollState
            ) {
                item {
                    Column {
                        ReviewedPlaceCard(
                            modifier = Modifier,
                            name = detailPlace.mapPlace.name,
                            rating = detailPlace.mapPlace.averageStarRating,
                            distance = detailPlace.mapPlace.distance,
                            reviewCount = detailPlace.mapPlace.reviewCount,
                            reviewerProfileImageUrls = detailPlace.mapPlace.reviewerProfileImageUrls,
                            reviewImageUrls = detailPlace.mapPlace.reviewImageUrls,
                        )

                        PHorizontalDivider(
                            modifier = Modifier
                                .height(10.dp)
                                .background(color = Colors.Neutral50)
                        )
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 7.5.dp)
                                .align(Alignment.CenterStart),
                            text = "핀버디 리뷰",
                            style = Typography.H3,
                            color = Colors.Neutral800
                        )

                        RoundedBox(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clickableSingleWithNoRipple {
                                    onMoveWriteReview()
                                },
                            backgroundColor = Colors.Neutral800,
                            cornerRounded = 100,
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .padding(vertical = 10.dp)
                                    .align(Alignment.Center),
                                text = "리뷰 작성",
                                style = Typography.H4,
                                color = Colors.White
                            )
                        }
                    }
                }

                item {
                    RoundedBox(
                        modifier = Modifier
                            .fillMaxWidth(),
                        cornerRounded = 8,
                        backgroundColor = Colors.Neutral50
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = detailPlace.mapPlace.averageStarRating.toString(),
                                        style = Typography.H1,
                                        color = Colors.Neutral800,
                                    )

                                    Text(
                                        modifier = Modifier
                                            .padding(start = 7.dp),
                                        text = "/ 5",
                                        style = Typography.H3,
                                        color = Colors.Neutral400,
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(top = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = detailPlace.mapPlace.reviewCount.toString(),
                                        style = Typography.B5,
                                        color = Colors.Neutral800,
                                    )
                                    Text(
                                        text = "명의 후기",
                                        style = Typography.B5,
                                        color = Colors.Neutral400,
                                    )
                                }
                            }

                            RatingGraphView(
                                modifier = Modifier
                                    .padding(vertical = 12.dp),
                                ratingGraph = detailPlace.ratingGraph,
                                totalReviewCount = detailPlace.mapPlace.reviewCount
                            )
                        }
                    }
                }

                items(detailPlace.placeReviews) {
                    ReviewCard(
                        modifier = Modifier,
                        placeReview = it,
                        horizontalPadding = 20.dp
                    )
                }
            }
        }
    }
}
