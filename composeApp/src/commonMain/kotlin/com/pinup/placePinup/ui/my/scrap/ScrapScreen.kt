package com.pinup.placePinup.ui.my.scrap
import org.jetbrains.compose.resources.stringResource

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.ui.component.Chips
import com.pinup.placePinup.ui.component.PHorizontalDivider
import com.pinup.placePinup.ui.component.ScrapDetailItemView
import com.pinup.placePinup.ui.component.SortBottomSheet
import com.pinup.placePinup.ui.component.TitleBar
import com.pinup.placePinup.ui.model.ChipState
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import pinup.composeapp.generated.resources.ic_alarm
import pinup.composeapp.generated.resources.ic_chevron_bottom
import pinup.composeapp.generated.resources.ic_right_arrow
import pinup.composeapp.generated.resources.ic_search
import pinup.composeapp.generated.resources.ic_setting

@Composable
fun ScrapScreen(
    scrapList: List<BookmarkedPlace>,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    onUpdateSortType: (SortType) -> Unit = {},
    onClickGoFeed: () -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onMovePlaceDetail: (String) -> Unit = {},
    onBackPressed: () -> Unit
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            SortBottomSheet(
                selectedSortType = sortType,
                allPermissionsGranted = true,
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
            modifier = Modifier
                .background(
                    color = Colors.White
                )
                .statusBarsPadding()
                .fillMaxWidth()
        ) {
            TitleBar(
                modifier = Modifier
                    .padding(horizontal = 20.dp),
                title = stringResource(Res.string.word_scrap),
                onLeftButtonClick = onBackPressed,
            )

            Spacer(modifier = Modifier.height(16.dp))


            if (scrapList.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
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
                                scope.launch { sheetState.show() }
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

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(13.dp),
                ) {
                    items(scrapList) { it ->
                        ScrapDetailItemView(
                            place = it,
                            onMovePlaceDetail = onMovePlaceDetail
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.profile_empty_scrap),
                        style = Typography.B1.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Colors.Gray400,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onClickGoFeed()
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(Res.string.profile_go_pinlog),
                            style = Typography.B2.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Colors.Gray600,
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Image(
                            modifier = Modifier.size(14.dp),
                            painter = painterResource(Res.drawable.ic_right_arrow),
                            colorFilter = ColorFilter.tint(Colors.Gray600),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}
