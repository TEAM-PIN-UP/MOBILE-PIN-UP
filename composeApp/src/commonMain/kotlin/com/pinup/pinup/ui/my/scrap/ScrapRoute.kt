package com.pinup.pinup.ui.my.scrap

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.main.compose.MainDestination
import kotlinx.collections.immutable.toPersistentList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScrapRoute(
    viewModel: ScrapViewModel = koinViewModel(),
    onClickBottomNav: (MainDestination) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    ScrapScreen(
        scrapList = emptyList(),
        chipStates = uiState.value.chipStates.toPersistentList(),
        sortType = uiState.value.sortType,
        profileUrl = uiState.value.profileUrl,
        onClickBottomNav = onClickBottomNav
    )
}