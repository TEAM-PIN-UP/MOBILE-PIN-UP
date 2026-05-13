package com.pinup.placePinup.ui.bookmark

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.ui.component.BookmarkedPlaceCard
import com.pinup.placePinup.ui.component.Chips
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.SortBottomSheet
import com.pinup.placePinup.ui.model.ChipState
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*

@Composable
fun BookmarkScreen(
    bookmarkedPlaces: PersistentList<BookmarkedPlace>,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    permissionState: Boolean,
    modifier: Modifier = Modifier,
    initBookmarkedPlaces: () -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onUpdateSortType: (SortType) -> Unit = {},
    onUpdateBookmark: (String) -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val pageAll = stringResource(Res.string.category_all)
    val pageByRegion = stringResource(Res.string.bookmark_tab_by_region)
    val pages = remember(pageAll, pageByRegion) { listOf(pageAll, pageByRegion) }
    val pagerState = rememberPagerState{ pages.size }

    LifecycleResumeEffect(Unit) {
        hLog("북마크 업데이트")
        initBookmarkedPlaces()
        onPauseOrDispose { }
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            SortBottomSheet(
                selectedSortType = sortType,
                allPermissionsGranted = permissionState,
                onSortTypeSelect = {
                    scope.launch {
                        sheetState.hide()
                        onUpdateSortType(it)
                    }
                }
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = Colors.White
                )
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 13.dp)
                    .padding(start = 20.dp),
                text = stringResource(Res.string.bookmark_title),
                style = Typography.H2,
                color = Colors.Neutral800
            )

            PHorizontalDivider()

            LazyRow(
                modifier = Modifier
                    .padding(top = 4.dp, start = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(pages) { index, item ->
                    Column(
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .clickableWithNoRipple {
                                scope.launch {
                                    pagerState.scrollToPage(index)
                                }
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .width(IntrinsicSize.Max)
                                .padding(top = 12.dp, bottom = 8.dp),
                            text = item,
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

            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
            ) {
                if (it == 0) {
                    BookmarkedPlaceAll(
                        bookmarkedPlaces = bookmarkedPlaces,
                        chipStates = chipStates,
                        sortType = sortType,
                        onChipClick = onChipClick,
                        onSortTypeClick = {
                            scope.launch {
                                sheetState.show()
                            }
                        },
                        onUpdateBookmark = onUpdateBookmark
                    )
                } else {

                }
            }
        }
    }
}

@Composable
fun BookmarkedPlaceAll(
    bookmarkedPlaces: PersistentList<BookmarkedPlace>,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    onChipClick: (ChipState) -> Unit = {},
    onSortTypeClick: () -> Unit = {},
    onUpdateBookmark: (String) -> Unit = {},
) {
    val lazyGridState = rememberLazyGridState()
    Column {
        Row(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 20.dp)
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
                        onSortTypeClick()
                    },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(sortType.textRes),
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

        if (bookmarkedPlaces.isEmpty()) {
            // emptyScreen
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                state = lazyGridState,
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(13.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(bookmarkedPlaces) {
                    BookmarkedPlaceCard(
                        bookmarkedPlace = it,
                        onUpdateBookmark = onUpdateBookmark
                    )
                }
            }
        }

    }
}