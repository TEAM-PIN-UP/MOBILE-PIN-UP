package com.pinup.placePinup.platform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitViewController
import com.pinup.placePinup.LocalNativeViewFactory
import com.pinup.placePinup.domain.model.CameraState
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.ui.map.MapViewModel
import com.pinup.placePinup.ui.map.PinchUiState
import com.pinup.placePinup.ui.map.PlaceDetailUiState
import com.pinup.placePinup.ui.map.SearchUiState

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
    onCameraStateChange: (CameraState) -> Unit,
    onMapClick: () -> Unit
) {
    val factory = LocalNativeViewFactory.current
    LaunchedEffect(key1 = cameraPosition) {
        hLog("cameraPosition>>> $cameraPosition")
    }

    //TODO 네이버 맵에도 터치 이벤트 달아야 함.
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
        },
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative // 지연 없이 즉시 네이티브에 위임
        )
    )
}
