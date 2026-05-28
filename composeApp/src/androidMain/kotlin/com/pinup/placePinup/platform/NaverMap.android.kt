package com.pinup.placePinup.platform

import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraUpdateReason
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.clustering.Clusterer
import com.naver.maps.map.clustering.ClusteringKey
import com.naver.maps.map.compose.DisposableMapEffect
import com.naver.maps.map.compose.LocationOverlay
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PolylineOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.pinup.placePinup.R
import com.pinup.placePinup.domain.model.CameraState
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.CategoryGroup
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.domain.model.PositionBounds
import com.pinup.placePinup.extentions.toLatLng
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.map.MapViewModel
import com.pinup.placePinup.ui.map.PinchUiState
import com.pinup.placePinup.ui.map.PlaceDetailUiState
import com.pinup.placePinup.ui.map.SearchUiState
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_cafe_marker_on
import pinup.composeapp.generated.resources.ic_cafe_marker_pinch
import pinup.composeapp.generated.resources.ic_food_marker_on
import pinup.composeapp.generated.resources.ic_food_marker_pinch

private data class ClusterPlaceItem(
    val kakaoPlaceId: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val markerResId: Int,
    val selectedMarkerResId: Int,
    val isSelected: Boolean,
) : ClusteringKey {
    override fun getPosition(): LatLng = LatLng(latitude, longitude)
}

private fun markerResIdByCategory(category: Category): Int {
    return when (category.group) {
        CategoryGroup.FNB -> R.drawable.ic_place_circle_red
        CategoryGroup.NATURE -> R.drawable.ic_place_circle_green
        CategoryGroup.CULTURE -> R.drawable.ic_place_circle_blue
        CategoryGroup.ETC -> R.drawable.ic_place_circle_black
    }
}

private fun selectedMarkerResIdByCategory(category: Category): Int = markerResIdByCategory(category)

private fun markerTint(group: CategoryGroup): ColorFilter? = when (group) {
    CategoryGroup.FNB -> null
    CategoryGroup.NATURE -> ColorFilter.tint(Color(0xFF3AC510))
    CategoryGroup.CULTURE -> ColorFilter.tint(Color(0xFF0096F3))
    CategoryGroup.ETC -> ColorFilter.tint(Color(0xFF1A1A1A))
}

@OptIn(ExperimentalNaverMapApi::class)
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
    onMapClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(cameraPosition) {
        cameraPosition?.let {
            val nowCameraPosition = Position(
                latitude = cameraPositionState.position.target.latitude,
                longitude = cameraPositionState.position.target.longitude
            )
//            hLog("카메라 이동됨 >>> 현재 카메라: $nowCameraPosition")
//            hLog("카메라 이동됨 >>> 바뀐 카메라: $cameraPosition")
//            hLog("카메라 이동됨 >>> 결과: ${if (it == nowCameraPosition) "같음, 취소 됨" else "다름, 이동 됨"}")
            if (it == nowCameraPosition) return@let
            scope.launch {
                cameraPositionState.animate(
                    CameraUpdate.scrollTo(it.toLatLng())
                )
            }
        }
    }

    LaunchedEffect(cameraPositionState.isMoving, cameraPositionState.contentBounds) {
        //hLog("MapScreen: ${cameraPositionState.cameraUpdateReason}")
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

    val selectedPlaceId = placeDetailUiState.detailPlace?.mapPlace?.kakaoPlaceId
    val clusterItems = if (!isShowPinch) {
        searchUiState.reviewedPlaces.map {
            ClusterPlaceItem(
                kakaoPlaceId = it.kakaoPlaceId,
                name = it.name,
                latitude = it.latitude,
                longitude = it.longitude,
                markerResId = markerResIdByCategory(it.placeCategory),
                selectedMarkerResId = selectedMarkerResIdByCategory(it.placeCategory),
                isSelected = it.kakaoPlaceId == selectedPlaceId,
            )
        }
    } else {
        emptyList()
    }

    NaverMap(
        modifier = Modifier
            .fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { _, _ ->
            onMapClick()
        },
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
            )
        }

        if (isShowPinch && pinchUiState.editorPintsDetail.pintsPlaceList.isNotEmpty()) {
            PolylineOverlay(
                coords = pinchUiState.editorPintsDetail.pintsPlaceList.map {
                    LatLng(it.latitude, it.longitude)
                },
                width = 1.dp,
                color = Colors.Negative,
                pattern = arrayOf(1.dp, 3.dp)
            )
            pinchUiState.editorPintsDetail.pintsPlaceList.forEach {
                key(
                    it.kakaoPlaceId,
                    it.kakaoPlaceId == placeDetailUiState.detailPlace?.mapPlace?.kakaoPlaceId
                ) {
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
                            val tint = markerTint(it.pintsPlaceCategory.group)
                            if (it.kakaoPlaceId == placeDetailUiState.detailPlace?.mapPlace?.kakaoPlaceId) {
                                Image(
                                    painter = when (it.pintsPlaceCategory) {
                                        Category.CAFE -> painterResource(Res.drawable.ic_cafe_marker_on)
                                        else -> painterResource(Res.drawable.ic_food_marker_on)
                                    },
                                    colorFilter = tint,
                                    contentDescription = "marker"
                                )
                            } else {
                                Image(
                                    painter = when (it.pintsPlaceCategory) {
                                        Category.CAFE -> painterResource(Res.drawable.ic_cafe_marker_pinch)
                                        else -> painterResource(Res.drawable.ic_food_marker_pinch)
                                    },
                                    colorFilter = tint,
                                    contentDescription = "marker"
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            RoundedBox(
                                modifier = Modifier,
                                backgroundColor = Colors.Black_25,
                                cornerColor = Color(0xFF3C3C3C),
                                cornerRounded = 100
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(vertical = 2.dp, horizontal = 6.dp)
                                        .widthIn(max = 72.dp),
                                    text = it.name,
                                    style = Typography.L3.copy(fontWeight = FontWeight.W600),
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

        if (!isShowPinch) {
            DisposableMapEffect(clusterItems, selectedPlaceId) { naverMap ->
                val clusterer = Clusterer.Builder<ClusterPlaceItem>()
                    .leafMarkerUpdater { info, marker ->
                        val item = info.tag as? ClusterPlaceItem ?: return@leafMarkerUpdater
                        marker.icon = com.naver.maps.map.overlay.OverlayImage.fromResource(
                            if (item.isSelected) item.selectedMarkerResId else item.markerResId
                        )
                        marker.iconTintColor = android.graphics.Color.TRANSPARENT
                        marker.anchor = android.graphics.PointF(0.5f, 0.5f)
                        marker.captionText = item.name
                        marker.captionColor = android.graphics.Color.WHITE
                        marker.captionHaloColor = android.graphics.Color.parseColor("#3C3C3C")
                        marker.captionOffset = 4
                        marker.captionRequestedWidth = 240
                        marker.captionTextSize = 10f
                        marker.setOnClickListener {
                            onPlaceClick(item.kakaoPlaceId)
                            true
                        }
                    }
                    .build()

                clusterer.setMap(naverMap)

                val mapItems = clusterItems.associateWith { item -> item }
                clusterer.clear()
                clusterer.addAll(mapItems)

                onDispose {
                    clusterer.clear()
                    clusterer.setMap(null)
                }
            }
        }
    }
}