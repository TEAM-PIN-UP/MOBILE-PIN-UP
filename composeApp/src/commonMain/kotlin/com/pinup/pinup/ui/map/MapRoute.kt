package com.pinup.pinup.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.pinlogDetail.PinlogUiEvent
import com.pinup.pinup.ui.theme.Texts
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import rememberSimpleToastState

@Composable
fun MapRoute(
    onBottomMenuClick: (MainDestination) -> Unit,
    onClickEdit: (Int) -> Unit = {},
) {
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )
    val mapViewModel: MapViewModel = koinViewModel(parameters = { parametersOf(locationTrackerFactory.createLocationTracker()) })
    val mapUiState = mapViewModel.uiState.collectAsStateWithLifecycle()
    val toast = rememberSimpleToastState()

    LaunchedEffect(Unit) {
        mapViewModel.uiEvent.collectLatest {
            when(it) {
                MapUiEvent.SuccessDelete -> {
                    toast.show(Texts.Toast.DELETE_PINLOG)
                }
            }
        }
    }

    BindLocationTrackerEffect(mapViewModel.locationTracker)
    MapScreen(
        viewModel = mapViewModel,
        searchUiState = mapUiState.value.searchUiState,
        placeDetailUiState = mapUiState.value.placeDetailUiState,
        position = mapUiState.value.currentPosition ?: Position.INVALID,
        cameraPosition = mapUiState.value.cameraPosition,
        isFocusLocation = mapUiState.value.isFocusLocation,
        isShowPinch = mapUiState.value.isShowPinch,
        isCameraMoving = mapUiState.value.isCameraMoving,
        isDetailClicked = mapUiState.value.isDetailClicked,
        pinchUiState = mapUiState.value.pinchUiState,
        clearPinchList = mapViewModel::clearPinchDetailList,
        onClickBottomNav = onBottomMenuClick,
        onCameraStateChange = mapViewModel::updateCameraState,
        onChipClick = mapViewModel::updateChipState,
        onValueChange = mapViewModel::updateSearchText,
        onPlaceClick = mapViewModel::getDetailPlace,
        onClearDetailPlace = mapViewModel::clearDetailPlace,
        onPinchListClick = mapViewModel::getPinchDetailList,
        onUpdateBookmark = mapViewModel::updateBookmark,
        onUpdateSortType = mapViewModel::updateSortType,
        onUpdatePosition = mapViewModel::collectPosition,
        onUpdateShowBookmarks = mapViewModel::updateShowPinch,
        onUpdateFocusLocation = mapViewModel::updateFocusLocation,
        onFocusChange = mapViewModel::updateFocusSearch,
        consumeDetailClicked = mapViewModel::consumedDetailClicked,
        onClickGetPlace = mapViewModel::getPlaces,
        onClickEdit = onClickEdit,
        onClickDelete = mapViewModel::deleteReview,
    )
}