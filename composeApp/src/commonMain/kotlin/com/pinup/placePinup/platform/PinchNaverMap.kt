package com.pinup.placePinup.platform

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pinup.placePinup.domain.model.CameraState
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.model.Position

@OptIn(ExperimentalMaterialApi::class)
@Composable
expect fun PinchNaverMap(
    modifier: Modifier,
    position: Position,
    placeList: List<Place>,
    cameraPosition: Position?,
    onCameraStateChange: (CameraState) -> Unit
)