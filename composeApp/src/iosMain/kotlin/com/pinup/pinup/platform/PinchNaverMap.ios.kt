package com.pinup.pinup.platform

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitViewController
import com.pinup.pinup.LocalNativeViewFactory
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun PinchNaverMap(
    modifier: Modifier,
    position: Position,
    placeList: List<Place>,
    cameraPosition: Position?,
    onCameraStateChange: (CameraState) -> Unit
) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        factory = {
            factory.createPintsNaverMap(
                placeList = placeList,
                cameraPosition = cameraPosition
            )
        },
        update = { controller ->
            factory.updatePintsNaverMap(
                controller = controller,
                placeList = placeList,
                cameraPosition = cameraPosition
            )
        },
        properties = UIKitInteropProperties(
            interactionMode = UIKitInteropInteractionMode.NonCooperative
        )
    )
}