package com.pinup.pinup.platform

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.ui.map.MapViewModel
import com.pinup.pinup.ui.map.PlaceDetailUiState
import com.pinup.pinup.ui.map.SearchUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
expect fun PlatformNaverMap(
    modifier: Modifier,
    viewModel: MapViewModel,
    position: Position,
    searchUiState: SearchUiState,
    pinchDetailList: List<ReviewedPlace>,
    placeDetailUiState: PlaceDetailUiState,
    isShowPinch: Boolean,
    cameraPosition: Position?,
    onPlaceClick: (String) -> Unit,
    onCameraStateChange: (CameraState) -> Unit
)