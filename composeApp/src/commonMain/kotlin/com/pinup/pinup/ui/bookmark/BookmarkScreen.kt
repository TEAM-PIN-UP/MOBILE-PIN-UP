package com.pinup.pinup.ui.bookmark

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.BookmarkedPlaceCard
import com.pinup.pinup.ui.component.Chips
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.SortBottomSheet
import com.pinup.pinup.ui.model.ChipState
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*

@Composable
fun BookmarkScreen(
    bookmarkedPlaces: PersistentList<BookmarkedPlace>,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    modifier: Modifier = Modifier,
    onChipClick: (ChipState) -> Unit = {},
    onUpdateSortType: (SortType) -> Unit = {},
    scope: CoroutineScope = rememberCoroutineScope()
) {
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    val pages = remember { listOf("전체","지역별") }
    val pagerState = rememberPagerState{ pages.size }
    Column(
        modifier = modifier
            .background(
                color = Colors.White
            )
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 13.dp)
                .padding(start = 20.dp),
            text = "마이 플레이스",
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
                        showBottomSheet = true
                    }
                )
            } else {

            }
        }
    }

    if (showBottomSheet) {
        SortBottomSheet(
            selectedSortType = sortType,
            allPermissionsGranted = permissionState.allPermissionsGranted,
            onDismissRequest = { showBottomSheet = false },
            onSortTypeSelect = {
                onUpdateSortType(it)
            }
        )
    }
}

@Composable
fun BookmarkedPlaceAll(
    bookmarkedPlaces: PersistentList<BookmarkedPlace>,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    onChipClick: (ChipState) -> Unit = {},
    onSortTypeClick: () -> Unit = {},
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
                        bookmarkedPlace = it
                    )
                }
            }
        }

    }
}