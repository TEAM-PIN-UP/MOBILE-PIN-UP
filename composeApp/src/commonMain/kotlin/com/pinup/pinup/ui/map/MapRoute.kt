package com.pinup.pinup.ui.map

import androidx.compose.runtime.Composable
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.naver.maps.geometry.LatLng

@Composable
fun MapRoute(
    mapViewModel: MapViewModel = koinViewModel()
) {
    val mapUiState = mapViewModel.mapUiState.collectAsStateWithLifecycle()

    MapScreen(
        searchUiState = mapUiState.value.searchUiState,
        placeDetailUiState = mapUiState.value.placeDetailUiState,
        position = mapUiState.value.currentLatLng ?: LatLng.INVALID,
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