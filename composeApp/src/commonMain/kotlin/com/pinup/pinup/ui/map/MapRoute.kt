package com.pinup.pinup.ui.map

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.domain.model.Position
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MapRoute() {
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )
    val mapViewModel: MapViewModel = koinViewModel(parameters = { parametersOf(locationTrackerFactory.createLocationTracker()) })
    val mapUiState = mapViewModel.mapUiState.collectAsStateWithLifecycle()

    MapScreen(
        viewModel = mapViewModel,
        searchUiState = mapUiState.value.searchUiState,
        placeDetailUiState = mapUiState.value.placeDetailUiState,
        position = mapUiState.value.currentPosition ?: Position.INVALID,
        cameraPosition = mapUiState.value.cameraPosition,
        isFocusLocation = mapUiState.value.isFocusLocation,
        isShowBookmarks = mapUiState.value.isShowBookmarks,
        onCameraStateChange = mapViewModel::getPlaces,
        onChipClick = mapViewModel::updateChipState,
        onPlaceClick = mapViewModel::getDetailPlace,
        onClearDetailPlace = mapViewModel::clearDetailPlace,
        onUpdateBookmark = mapViewModel::updateBookmark,
        onUpdateSortType = mapViewModel::updateSortType,
        onUpdatePosition = mapViewModel::collectPosition,
        onUpdateShowBookmarks = mapViewModel::updateShowBookmarks,
        onUpdateFocusLocation = mapViewModel::updateFocusLocation,
    )
}