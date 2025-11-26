package com.pinup.placePinup.platform

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pinup.placePinup.domain.model.CameraState
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.ui.map.MapViewModel
import com.pinup.placePinup.ui.map.PinchUiState
import com.pinup.placePinup.ui.map.PlaceDetailUiState
import com.pinup.placePinup.ui.map.SearchUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
expect fun PlatformNaverMap(
    modifier: Modifier,
    viewModel: MapViewModel,
    position: Position,
    searchUiState: SearchUiState,
    pinchUiState: PinchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isShowPinch: Boolean,
    cameraPosition: Position?,
    onPlaceClick: (String) -> Unit,
    onCameraStateChange: (CameraState) -> Unit,
    onMapClick: () -> Unit
)