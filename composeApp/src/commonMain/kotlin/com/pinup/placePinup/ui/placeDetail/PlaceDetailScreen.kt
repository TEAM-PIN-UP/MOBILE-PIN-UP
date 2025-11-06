package com.pinup.placePinup.ui.placeDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.DetailPlace
import com.pinup.placePinup.domain.model.ReviewedPlace.Companion.MapToPlace
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.PinlogMenuBottomSheet
import com.pinup.placePinup.ui.component.ReviewCard
import com.pinup.placePinup.ui.component.ReviewedPlaceCard
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_back
import pinup.composeapp.generated.resources.ic_bookmark_off
import pinup.composeapp.generated.resources.ic_bookmark_on
import pinup.composeapp.generated.resources.ic_write_pinlog

@Composable
fun PlaceDetailScreen(
    detailPlace: DetailPlace,
    onMoveWriteReview: (String) -> Unit = {},
    onBackPressed: () -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
    onClickEdit: (Int) -> Unit = {},
    onClickDelete: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ ->},
    onMovePinlogDetail: (Int) -> Unit = {},
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val scrollState = rememberLazyListState()
    var clickedPinlogId by remember { mutableIntStateOf(0) }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            PinlogMenuBottomSheet(
                onClickEdit = {
                    onClickEdit(clickedPinlogId)
                    scope.launch { sheetState.hide() }
                },
                onClickDelete = {
                    onClickDelete(clickedPinlogId)
                    scope.launch { sheetState.hide() }
                },
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Colors.White
                )
                .statusBarsPadding()
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickableWithNoRipple {
                            onBackPressed()
                        },
                    painter = painterResource(Res.drawable.ic_back),
                    contentDescription = "bottomsheet back"
                )

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickableSingleWithNoRipple {
                            onUpdateBookmark(
                                detailPlace.mapPlace.kakaoPlaceId,
                                detailPlace.mapPlace.bookmark
                            )
                        },
                    painter = if (detailPlace.mapPlace.bookmark) {
                        painterResource(Res.drawable.ic_bookmark_on)
                    } else {
                        painterResource(Res.drawable.ic_bookmark_off)
                    },
                    contentDescription = "boomark"
                )

                Spacer(modifier = Modifier.width(10.dp))

                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickableSingleWithNoRipple {
                            onMoveWriteReview(Json.encodeToString(value = detailPlace.mapPlace.MapToPlace()))
                        },
                    painter = painterResource(Res.drawable.ic_write_pinlog),
                    contentDescription = "pinlog"
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding(),
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
                        onClickMenu = {
                            clickedPinlogId = it
                            scope.launch { sheetState.show() }
                        },
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
