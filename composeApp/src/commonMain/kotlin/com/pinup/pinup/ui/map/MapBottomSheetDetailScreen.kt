package com.pinup.pinup.ui.map

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.RatingGraphView
import com.pinup.pinup.ui.component.ReviewCard
import com.pinup.pinup.ui.component.ReviewedPlaceCard
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MapBottomSheetDetailScreen(
    detailPlace: DetailPlace?,
    isExpanded: Boolean,
    onMoveWriteReview: (ReviewedPlace) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onClearDetailPlace: () -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
    onClickMenu: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ ->},
    onMovePinlogDetail: (Int) -> Unit = {},
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
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickableWithNoRipple {
                            onClearDetailPlace()
                            onBackPressed()
                        },
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = "bottomsheet back"
                )

                Spacer(modifier = Modifier.weight(1f))

                RoundedBox(
                    modifier = Modifier
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

                Spacer(modifier = Modifier.width(8.dp))

                RoundedBox(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            onMoveWriteReview(detailPlace.mapPlace)
                        },
                    backgroundColor = Colors.Neutral50,
                    cornerRounded = 100
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(10.dp),
                        painter = painterResource(Res.drawable.ic_write_pinlog),
                        contentDescription = "pinlog"
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
                                .height(8.dp)
                                .background(color = Colors.Neutral50)
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .padding(start = 20.dp)
                    ) {
                        Text(
                            text = Texts.Word.PINLOG + " " + detailPlace.mapPlace.reviewCount.toString(),
                            style = Typography.B1.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Gray800
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(detailPlace.placeReviews) {
                    ReviewCard(
                        modifier = Modifier,
                        placeReview = it,
                        horizontalPadding = 20.dp,
                        onClickMenu = onClickMenu,
                        onClickLike = onClickLike,
                        onMovePinlogDetail = onMovePinlogDetail
                    )

                    PHorizontalDivider(
                        modifier = Modifier
                            .height(1.dp)
                            .background(color = Colors.Neutral50)
                    )
                }
            }
        }
    }
}
