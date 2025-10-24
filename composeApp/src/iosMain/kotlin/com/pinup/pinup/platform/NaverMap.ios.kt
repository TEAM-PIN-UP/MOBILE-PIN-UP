package com.pinup.pinup.platform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import androidx.compose.ui.viewinterop.UIKitViewController
import com.pinup.pinup.LocalNativeViewFactory
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.ui.map.MapViewModel
import com.pinup.pinup.ui.map.PinchUiState
import com.pinup.pinup.ui.map.PlaceDetailUiState
import com.pinup.pinup.ui.map.SearchUiState
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun PlatformNaverMap(
    modifier: Modifier,
    viewModel: MapViewModel,
    position: Position,
    searchUiState: SearchUiState,
    pinchUiState: PinchUiState,
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
            .clickableWithNoRipple {
                print("터치요")
            }
            .fillMaxSize(),
        factory = {
            factory.createNaverMap(
                viewModel = viewModel
            )
        },
        update = {
            // some logic
        },
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative // 지연 없이 즉시 네이티브에 위임
        )
    )
}
