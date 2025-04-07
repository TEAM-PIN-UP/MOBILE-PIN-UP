package com.pinup.pinup.platform

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraUpdateReason
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationOverlay
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.rememberCameraPositionState
import com.naver.maps.map.overlay.OverlayImage
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.PositionBounds
import com.pinup.pinup.extentions.toLatLng
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.map.MapViewModel
import com.pinup.pinup.ui.map.PlaceDetailUiState
import com.pinup.pinup.ui.map.SearchUiState
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.imageResource
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_cafe_marker
import pinup.composeapp.generated.resources.ic_food_marker
import pinup.composeapp.generated.resources.ic_my_position

@OptIn(ExperimentalNaverMapApi::class)
@Composable
actual fun PlatformNaverMap(
    modifier: Modifier,
    viewModel: MapViewModel,
    position: Position,
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isShowBookmarks: Boolean,
    cameraPosition: Position?,
    onPlaceClick: (String) -> Unit,
    onCameraStateChange: (CameraState) -> Unit
) {
    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(cameraPosition) {
        hLog("cameraPosition >>> $cameraPosition")
        cameraPosition?.let {
            scope.launch {
                cameraPositionState.animate(
                    CameraUpdate.scrollTo(it.toLatLng())
                )
            }
        }
    }

    LaunchedEffect(cameraPositionState.isMoving) {
        hLog("MapScreen: ${cameraPositionState.cameraUpdateReason}")
        cameraPositionState.contentBounds?.let {
            onCameraStateChange(
                CameraState(
                    isMoving = cameraPositionState.isMoving,
                    contentBounds = PositionBounds(
                        southWest = Position(
                            latitude = it.southWest.latitude,
                            longitude = it.southWest.longitude
                        ),
                        northEast = Position(
                            latitude = it.northEast.latitude,
                            longitude = it.northEast.longitude
                        )
                    ),
                    reason = if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE) {
                        CameraState.Reason.GESTURE
                    } else {
                        CameraState.Reason.ELSE
                    },
                    position = Position(
                        latitude = cameraPositionState.position.target.latitude,
                        longitude = cameraPositionState.position.target.longitude
                    )
                )
            )
        }
    }

    NaverMap(
        modifier = Modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            extent = LatLngBounds(
                LatLng(33.0, 124.0),
                LatLng(38.5, 132.0)
            ),
            minZoom = 5.0,
            locationTrackingMode = LocationTrackingMode.Follow
        ),
        uiSettings = MapUiSettings(
            isLocationButtonEnabled = false,
            isCompassEnabled = false,
            isZoomControlEnabled = false,
            isScaleBarEnabled = false,
            isRotateGesturesEnabled = false,
            logoGravity = Gravity.TOP and Gravity.START
        ),
    ) {
        if (position.isValid) {
            LocationOverlay(
                position = position.toLatLng(),
                icon = OverlayImage.fromBitmap(imageResource(Res.drawable.ic_my_position).asAndroidBitmap())
            )
        }

        searchUiState.reviewedPlaces.filter { if (isShowBookmarks) it.bookmark else true }.forEach {
            MarkerComposable(
                keys = arrayOf(it.kakaoPlaceId),
                state = MarkerState(
                    position = LatLng(it.latitude, it.longitude),
                ),
                anchor = Offset(0.5f, 0.25f),
                onClick = { _ ->
                    onPlaceClick(it.kakaoPlaceId)
                    true
                }
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (it.kakaoPlaceId == placeDetailUiState.detailPlace?.mapPlace?.kakaoPlaceId) {

                    } else {
                        Image(
                            painter = when (it.placeCategory) {
                                Category.RESTAURANT -> {
                                    painterResource(Res.drawable.ic_food_marker)
                                }

                                else -> {
                                    painterResource(Res.drawable.ic_cafe_marker)
                                }
                            },
                            contentDescription = "marker"
                        )
                    }

                    RoundedBox(
                        modifier = Modifier,
                        backgroundColor = Colors.Black_25,
                        cornerRounded = 100
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 2.dp, horizontal = 7.dp)
                                .widthIn(max = 44.dp),
                            text = it.name,
                            style = Typography.B6,
                            color = Colors.White,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}