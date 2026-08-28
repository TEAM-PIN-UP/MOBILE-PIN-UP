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
import androidx.compose.runtime.remember
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
import com.pinup.placePinup.util.MapZoom
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_cafe_marker
import pinup.composeapp.generated.resources.ic_food_marker

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

// 상세 시트가 화면 하단 약 절반을 덮으므로, 보이는 지도 영역(상단부)의 중앙은 전체 높이의 1/4 지점이다.
private const val DETAIL_FOCUS_PIVOT_Y = 0.25f

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
    cameraZoom: Double?,
    onPlaceClick: (String) -> Unit,
    onCameraStateChange: (CameraState) -> Unit,
    onMapClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val cameraPositionState = rememberCameraPositionState()
    LaunchedEffect(cameraPosition, cameraZoom) {
        cameraPosition?.let {
            val nowCameraPosition = Position(
                latitude = cameraPositionState.position.target.latitude,
                longitude = cameraPositionState.position.target.longitude
            )
//            hLog("카메라 이동됨 >>> 현재 카메라: $nowCameraPosition")
//            hLog("카메라 이동됨 >>> 바뀐 카메라: $cameraPosition")
//            hLog("카메라 이동됨 >>> 결과: ${if (it == nowCameraPosition) "같음, 취소 됨" else "다름, 이동 됨"}")
            // 요청된 줌이 현재보다 클 때만 확대한다. (이미 더 확대돼 있으면 현재 줌 유지)
            val targetZoom = cameraZoom?.takeIf { zoom -> zoom > cameraPositionState.position.zoom }
            if (it == nowCameraPosition && targetZoom == null) return@let
            // 상세·핀츠 시트가 떠 있으면 대상 좌표가 '시트 위로 보이는 영역'의 중앙에 오도록
            // 화면 좌표 pivot으로 보정한다. 기존의 위도 -0.0078 고정 오프셋(약 867m)은
            // 특정 줌에서만 맞아, 줌이 낮으면 마커가 시트 뒤로 숨고 높으면 화면 밖으로 나갔다.
            val focusOnVisibleArea = placeDetailUiState.detailPlace != null || isShowPinch
            scope.launch {
                val update = if (targetZoom != null) {
                    CameraUpdate.scrollAndZoomTo(it.toLatLng(), targetZoom)
                } else {
                    CameraUpdate.scrollTo(it.toLatLng())
                }
                cameraPositionState.animate(
                    if (focusOnVisibleArea) {
                        update.pivot(android.graphics.PointF(0.5f, DETAIL_FOCUS_PIVOT_Y))
                    } else {
                        update
                    }
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
    // remember 없이 매 리컴포지션마다 새 리스트를 만들면, 아래 DisposableMapEffect의 key가
    // 매번 달라져 지도를 드래그·줌할 때마다 Clusterer가 통째로 파괴·재생성되고 전체 마커를
    // 다시 addAll 하게 된다. 실제로 바뀔 때(장소 목록·선택 변경)만 새 리스트가 되도록 고정한다.
    val clusterItems = remember(searchUiState.reviewedPlaces, selectedPlaceId, isShowPinch) {
        if (!isShowPinch) {
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
                key(it.kakaoPlaceId) {
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
                            Image(
                                painter = when (it.pintsPlaceCategory) {
                                    Category.CAFE -> painterResource(Res.drawable.ic_cafe_marker)
                                    else -> painterResource(Res.drawable.ic_food_marker)
                                },
                                colorFilter = markerTint(it.pintsPlaceCategory.group),
                                contentDescription = "marker"
                            )

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
            DisposableMapEffect(clusterItems) { naverMap ->
                val clusterer = Clusterer.Builder<ClusterPlaceItem>()
                    // 축척 100m(줌 16)부터는 클러스터링 없이 모든 핀을 개별 노출
                    .maxZoom(MapZoom.MAX_CLUSTERING)
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

                val mapItems = clusterItems.associateWith { item: ClusterPlaceItem -> item }
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