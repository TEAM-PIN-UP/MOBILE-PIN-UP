package com.pinup.pinup.platform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import androidx.compose.ui.viewinterop.UIKitViewController
import com.pinup.pinup.LocalNativeViewFactory
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.ui.map.MapViewModel
import com.pinup.pinup.ui.map.PlaceDetailUiState
import com.pinup.pinup.ui.map.SearchUiState
import kotlinx.cinterop.ExperimentalForeignApi

@Composable
actual fun PlatformNaverMap(
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
) {
    val factory = LocalNativeViewFactory.current
    LaunchedEffect(key1 = cameraPosition) {
        // some logic
        hLog("cameraPosition>>> $cameraPosition")
    }

    UIKitViewController(
        modifier = modifier
            .fillMaxSize(),
        factory = {
            factory.createNaverMap(
                viewModel = viewModel
            )
        },
        update = {
            // some logic
        }
    )
}
