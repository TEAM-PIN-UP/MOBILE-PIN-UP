package com.pinup.pinup.ui.map

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pinup.pinup.R
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.extentions.toDp
import com.pinup.pinup.ui.component.Chips
import com.pinup.pinup.ui.component.ReviewedPlaceCard
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.SortBottomSheet
import com.pinup.pinup.ui.model.ChipState
import com.pinup.pinup.ui.theme.Colors

import com.pinup.pinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MapBottomSheetSearchScreen(
    reviewedPlaces: PersistentList<ReviewedPlace>,
    query: String,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    allPermissionsGranted: Boolean,
    isExpanded: Boolean,
    onValueChange: (String) -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (ReviewedPlace) -> Unit = {},
    onUpdateSortType: (SortType) -> Unit = {}
) {
    var showBottomSheet by remember { mutableStateOf(false) }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Colors.White
            )
    ) {
        RoundedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = query,
            onValueChange = onValueChange,
            placeholder = "장소/가게 검색하기",
            cornerRounded = 100,
            leadingIcon = if (query.isNotEmpty()) painterResource(Res.drawable.ic_search_back) else painterResource(
                Res.drawable.ic_search
            ),
            backgroundColor = Colors.Neutral50,
            unfocusedBorderColor = Colors.Neutral50,
            focusedBorderColor = Colors.Neutral50,
        )

        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 16.dp)
        ) {
            Chips(
                chipStates = chipStates,
                onClick = onChipClick
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickableWithNoRipple {
                        showBottomSheet = true
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = sortType.text,
                    style = Typography.B4
                )

                Image(
                    modifier = Modifier
                        .padding(start = 2.dp),
                    painter = painterResource(Res.drawable.ic_chevron_bottom),
                    contentDescription = null
                )
            }
        }

        if (reviewedPlaces.isEmpty()) {
            Text(
                modifier = Modifier
                    .padding(top = 80.dp)
                    .fillMaxWidth()
                    .background(color = Colors.White),
                text = "근처에 리뷰 있는\n가게가 없어요!",
                color = Colors.Neutral500,
                style = Typography.C2,
                textAlign = TextAlign.Center
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(
                        color = Colors.Neutral50
                    )
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
                state = scrollState,
                userScrollEnabled = userScrollable,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(reviewedPlaces) {
                    ReviewedPlaceCard(
                        name = it.name,
                        rating = it.averageStarRating,
                        distance = it.distance,
                        reviewCount = it.reviewCount,
                        reviewerProfileImageUrls = it.reviewerProfileImageUrls,
                        reviewImageUrls = it.reviewImageUrls,
                        onItemClick = {
                            onPlaceClick(it)
                        }
                    )
                }
            }
        }
    }

    if (showBottomSheet) {
        SortBottomSheet(
            selectedSortType = sortType,
            allPermissionsGranted = allPermissionsGranted,
            onDismissRequest = { showBottomSheet = false },
            onSortTypeSelect = {
                onUpdateSortType(it)
            }
        )
    }
}

@Preview
@Composable
private fun PlacesSheetContentPreview() {
    PinUPTheme {
        MapBottomSheetSearchScreen(
            query = "",
            chipStates = ChipState.default.toPersistentList(),
            sortType = SortType.NEAR,
            allPermissionsGranted = true,
            reviewedPlaces = Array(3) {
                ReviewedPlace(
                    name = "하우스서울 잠실새내",
                    averageStarRating = 4.0,
                    distance = "2.4",
                    reviewCount = 35,
                    reviewerProfileImageUrls = listOf(
                        "", "", "", ""
                    ),
                    reviewImageUrls = listOf(
                        "", "", "", ""
                    ),
                    longitude = 0.0,
                    latitude = 0.0,
                    placeCategory = Category.CAFE,
                    kakaoPlaceId = "",
                    bookmark = false
                )
            }.toPersistentList(),
            isExpanded = false
        )
    }
}

fun getNavigationBarHeight(context: Context): Dp {
    val resources = context.resources
    val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) resources.getDimensionPixelSize(resourceId).toDp(context).dp else 0.dp
}

@Preview
@Composable
private fun PlacesSheetContentEmptyPreview() {
    PinUPTheme {
        MapBottomSheetSearchScreen(
            query = "",
            chipStates = ChipState.default.toPersistentList(),
            sortType = SortType.NEAR,
            reviewedPlaces = emptyList<ReviewedPlace>().toPersistentList(),
            allPermissionsGranted = false,
            isExpanded = false
        )
    }
}