package com.pinup.pinup.ui.map

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.ui.main.compose.MainDestination
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MapRoute(
    onBottomMenuClick: (MainDestination) -> Unit
) {
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )
    val mapViewModel: MapViewModel = koinViewModel(parameters = { parametersOf(locationTrackerFactory.createLocationTracker()) })
    val mapUiState = mapViewModel.uiState.collectAsStateWithLifecycle()

    BindLocationTrackerEffect(mapViewModel.locationTracker)
    MapScreen(
        viewModel = mapViewModel,
        searchUiState = mapUiState.value.searchUiState,
        placeDetailUiState = mapUiState.value.placeDetailUiState,
        position = mapUiState.value.currentPosition ?: Position.INVALID,
        cameraPosition = mapUiState.value.cameraPosition,
        isFocusLocation = mapUiState.value.isFocusLocation,
        isShowPinch = mapUiState.value.isShowBookmarks,
        isCameraMoving = mapUiState.value.isCameraMoving,
        profileImage = mapUiState.value.profileImage,
        onClickBottomNav = onBottomMenuClick,
        onCameraStateChange = mapViewModel::getPlaces,
        onChipClick = mapViewModel::updateChipState,
        onValueChange = mapViewModel::updateSearchText,
        onPlaceClick = mapViewModel::getDetailPlace,
        onClearDetailPlace = mapViewModel::clearDetailPlace,
        onUpdateBookmark = mapViewModel::updateBookmark,
        onUpdateSortType = mapViewModel::updateSortType,
        onUpdatePosition = mapViewModel::collectPosition,
        onUpdateShowBookmarks = mapViewModel::updateShowBookmarks,
        onUpdateFocusLocation = mapViewModel::updateFocusLocation,
        onFocusChange = mapViewModel::updateFocusSearch
    )
}