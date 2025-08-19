package com.pinup.pinup.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.component.Chips
import com.pinup.pinup.ui.component.ReviewedPlaceCard
import com.pinup.pinup.ui.component.RoundedTextField
import com.pinup.pinup.ui.component.SearchedPlaceCard
import com.pinup.pinup.ui.component.SortBottomSheet
import com.pinup.pinup.ui.model.ChipState
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_chevron_bottom
import pinup.composeapp.generated.resources.ic_close
import pinup.composeapp.generated.resources.ic_search
import pinup.composeapp.generated.resources.ic_search_back

@Composable
fun MapBottomSheetSearchScreen(
    reviewedPlaces: PersistentList<ReviewedPlace>,
    query: String,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    isExpanded: Boolean,
    places: List<Place>,
    onValueChange: (String) -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (String) -> Unit = {},
    onSelectSortTypeClick: () -> Unit = {},
    onFocusChange: (Boolean) -> Unit ={},
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var isFocusSearch by remember {
        mutableStateOf(false)
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
            textStyle = Typography.B1.copy(
                fontWeight = FontWeight.Medium
            ),
            onValueChange = onValueChange,
            placeholder = Texts.PinMap.SEARCH_HINT,
            placeholderStyle = Typography.T2.copy(
                fontWeight = FontWeight.Medium
            ),
            placeholderTextColor = Colors.Gray400,
            cornerRounded = 100,
            leadingIcon = if (isFocusSearch) painterResource(Res.drawable.ic_search_back) else painterResource(
                Res.drawable.ic_search
            ),
            onLeadingIconClick = {
                focusManager.clearFocus(force = true)
            },
            tailIcon = if (query.isNotEmpty()) painterResource(Res.drawable.ic_close) else null,
            tailIconSize = 20,
            onTailIconClick = {
                onValueChange("")
            },
            fixedBorderColor = Colors.Transparency,
            backgroundColor = Colors.Gray50,
            onFocusChange = {
                isFocusSearch = it
                onFocusChange(it)
            },
            focusRequester = focusRequester
        )

        if (isFocusSearch) {
            FocusScreen(
                places = places,
                onPlaceClick = {
                    onPlaceClick(it.kakaoPlaceId)
                }
            )
        } else {
            NonFocusScreen(
                reviewedPlaces = reviewedPlaces,
                chipStates = chipStates,
                sortType = sortType,
                isExpanded = isExpanded,
                onChipClick = onChipClick,
                onPlaceClick = {
                    onPlaceClick(it.kakaoPlaceId)
                },
                onSelectSortTypeClick = onSelectSortTypeClick
            )
        }

    }
}

@Composable
fun NonFocusScreen(
    reviewedPlaces: PersistentList<ReviewedPlace>,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    isExpanded: Boolean,
    onChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (ReviewedPlace) -> Unit = {},
    onSelectSortTypeClick: () -> Unit = {},
){
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
                    onSelectSortTypeClick()
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
            text = Texts.PinMap.EMPTY_PINLOG,
            color = Colors.Neutral500,
            style = Typography.C2,
            textAlign = TextAlign.Center
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .background(
                    color = Colors.Gray50
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

@Composable
fun FocusScreen(
    places: List<Place>,
    onPlaceClick: (Place) -> Unit = {},
){
    val scrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = Texts.PinMap.SEARCH_RESULT,
            color = Colors.Neutral800,
            style = Typography.B2.copy(
                fontWeight = FontWeight.Medium
            ),
        )

        Spacer(modifier = Modifier.height(28.dp))

        LazyColumn(
            state = scrollState,
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            items(places) {
                SearchedPlaceCard(
                    name = it.name,
                    address = it.address,
                    category = Category.of(it.categoryCode),
                    reviewCount = it.reviewCount,
                    onClick = {
                        onPlaceClick(it)
                    }
                )
            }
        }
    }
}
