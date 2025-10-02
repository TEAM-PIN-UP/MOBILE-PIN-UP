package com.pinup.pinup.platform

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position

@OptIn(ExperimentalMaterialApi::class)
@Composable
expect fun PinchNaverMap(
    modifier: Modifier,
    position: Position,
    placeList: List<Place>,
    cameraPosition: Position?,
    onCameraStateChange: (CameraState) -> Unit
)