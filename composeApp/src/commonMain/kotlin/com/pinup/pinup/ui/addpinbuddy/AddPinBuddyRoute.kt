package com.pinup.pinup.ui.addpinbuddy

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.main.compose.MainDestination
import kotlinx.collections.immutable.toPersistentList

@Composable
fun AddPinBuddyRoute(
    onBackPressed: () -> Unit,
    onMoveUserProfile: (Int) -> Unit,
    viewModel: AddPinBuddyViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    AddPinBuddyScreen(
        query = uiState.value.query,
        pinBuddies = uiState.value.pinBuddies?.toPersistentList(),
        onValueChange = viewModel::updateQuery,
        onBackPressed = onBackPressed,
        onSearch = viewModel::search,
        onProfileClick = onMoveUserProfile,
        recentSearchList = uiState.value.recentSearchList,
        onClickDeleteRecentSearch = viewModel::deleteRecentSearch,
    )
}
