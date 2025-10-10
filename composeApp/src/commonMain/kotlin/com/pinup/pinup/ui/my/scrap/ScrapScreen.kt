package com.pinup.pinup.ui.my.scrap

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.Chips
import com.pinup.pinup.ui.component.PHorizontalDivider
import com.pinup.pinup.ui.component.ScrapDetailItemView
import com.pinup.pinup.ui.component.SortBottomSheet
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.model.ChipState
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Texts
import com.pinup.pinup.ui.theme.Typography
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_alarm
import pinup.composeapp.generated.resources.ic_chevron_bottom
import pinup.composeapp.generated.resources.ic_setting

@Composable
fun ScrapScreen(
    scrapList: List<BookmarkedPlace>,
    chipStates: PersistentList<ChipState>,
    sortType: SortType,
    profileUrl: String = "",
    onUpdateSortType: (SortType) -> Unit = {},
    onAlarmClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    onMovePlaceDetail: (String) -> Unit = {},
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    val density = LocalDensity.current
    var bottomBarHeight by remember { mutableStateOf(0.dp) }

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
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(vertical = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = Texts.Word.SCRAP,
                    style = Typography.T1.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Colors.Gray800
                )

                Spacer(Modifier.weight(1f))

                Image(
                    modifier = Modifier
                        .clickableSingleWithNoRipple {
                            onAlarmClick()
                        },
                    painter = painterResource(Res.drawable.ic_alarm),
                    contentDescription = "alarm"
                )

                Spacer(modifier = Modifier.width(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

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
                contentPadding = PaddingValues(bottom = bottomBarHeight)
            ) {
                items(scrapList) { it ->
                    ScrapDetailItemView(
                        place = it,
                        onMovePlaceDetail = onMovePlaceDetail
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar(
                selectedMenu = MainDestination.My,
                profileImage = profileUrl,
                onBottomMenuClick = onClickBottomNav,
                onSizeChanged = { bottomBarHeight = it }
            )

        }
    }
}
