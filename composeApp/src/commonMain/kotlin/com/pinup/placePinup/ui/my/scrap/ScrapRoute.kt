package com.pinup.placePinup.ui.my.scrap

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toPersistentList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScrapRoute(
    viewModel: ScrapViewModel = koinViewModel(),
    onMovePlaceDetail: (String) -> Unit = {},
    onSettingClick: () -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ScrapScreen(
        scrapList = uiState.value.scrapList,
        chipStates = uiState.value.chipStates.toPersistentList(),
        sortType = uiState.value.sortType,
        onMovePlaceDetail = onMovePlaceDetail,
        onChipClick = viewModel::updateChipState,
        onUpdateSortType = viewModel::updateSortType,
        onAlarmClick = { /*TODO 알림 화면 이동 */ },
        onSettingClick = onSettingClick,
    )
}