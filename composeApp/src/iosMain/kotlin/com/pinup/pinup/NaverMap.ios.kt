package com.pinup.pinup

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.ui.map.PlaceDetailUiState
import com.pinup.pinup.ui.map.SearchUiState
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class, ExperimentalMaterialApi::class)
@Composable
actual fun PlatformNaverMap(
    modifier: Modifier,
    position: Position,
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isShowBookmarks: Boolean,
    cameraPosition: Position?,
    onPlaceClick: (String) -> Unit
) {
    val naverMapView = remember { NMFMapView() }
    val markers = remember { mutableMapOf<Long, NMFMarker>() }

    LaunchedEffect(key1 = Unit) {
        // some logic
    }

    UIKitView(
        modifier = modifier,
        factory = {
            naverMapView
        },
        update = {
            // some logic
        }
    )
}
