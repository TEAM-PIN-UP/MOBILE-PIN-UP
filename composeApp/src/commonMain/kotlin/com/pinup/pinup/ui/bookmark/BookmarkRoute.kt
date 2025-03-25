package com.pinup.pinup.ui.bookmark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toPersistentList

@Composable
fun BookmarkRoute(
    bookmarkViewModel: BookmarkViewModel = koinViewModel(),
) {
    val uiState by bookmarkViewModel.uiState.collectAsStateWithLifecycle()
    BookmarkScreen(
        bookmarkedPlaces = uiState.bookmarkedPlace.toPersistentList(),
        chipStates = uiState.chipStates.toPersistentList(),
        sortType = uiState.sortType,
        onChipClick = bookmarkViewModel::updateFilterCategory,
        onUpdateSortType = bookmarkViewModel::updateSortType
    )
}
