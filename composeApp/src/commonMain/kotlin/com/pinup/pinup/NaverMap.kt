package com.pinup.pinup

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.ui.map.PlaceDetailUiState
import com.pinup.pinup.ui.map.SearchUiState

@OptIn(ExperimentalMaterialApi::class)
@Composable
expect fun PlatformNaverMap(
    modifier: Modifier,
    position: Position,
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isShowBookmarks: Boolean,
    cameraPosition: Position?,
    onPlaceClick: (String) -> Unit
)