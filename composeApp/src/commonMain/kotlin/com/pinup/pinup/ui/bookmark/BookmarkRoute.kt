package com.pinup.pinup.ui.bookmark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import kotlinx.collections.immutable.toPersistentList
import org.koin.core.parameter.parametersOf

@Composable
fun BookmarkRoute(
) {
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )
    val bookmarkViewModel: BookmarkViewModel = koinViewModel(parameters = { parametersOf(locationTrackerFactory.createLocationTracker()) })
    val uiState by bookmarkViewModel.uiState.collectAsStateWithLifecycle()

    BindLocationTrackerEffect(bookmarkViewModel.locationTracker)
    BookmarkScreen(
        bookmarkedPlaces = uiState.bookmarkedPlace.toPersistentList(),
        chipStates = uiState.chipStates.toPersistentList(),
        sortType = uiState.sortType,
        permissionState = uiState.permissionState,
        initBookmarkedPlaces = bookmarkViewModel::initBookmarkedPlaces,
        onChipClick = bookmarkViewModel::updateFilterCategory,
        onUpdateSortType = bookmarkViewModel::updateSortType,
        onUpdateBookmark = bookmarkViewModel::updateBookmark
    )
}
