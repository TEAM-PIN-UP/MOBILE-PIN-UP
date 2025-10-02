package com.pinup.pinup.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.ui.map.PlaceDetailUiState

@Composable
actual fun PinchNaverMap(
    modifier: Modifier,
    position: Position,
    placeList: List<Place>,
    cameraPosition: Position?,
    onCameraStateChange: (CameraState) -> Unit
) {

}