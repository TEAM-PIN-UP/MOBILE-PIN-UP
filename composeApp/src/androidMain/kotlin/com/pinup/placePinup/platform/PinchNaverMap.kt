package com.pinup.placePinup.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.LocationTrackingMode
import com.naver.maps.map.compose.MapProperties
import com.naver.maps.map.compose.MapUiSettings
import com.naver.maps.map.compose.MarkerComposable
import com.naver.maps.map.compose.MarkerState
import com.naver.maps.map.compose.NaverMap
import com.naver.maps.map.compose.PolylineOverlay
import com.naver.maps.map.compose.rememberCameraPositionState
import com.pinup.placePinup.domain.model.CameraState
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.extentions.toLatLng
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Typography
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_cafe_marker
import pinup.composeapp.generated.resources.ic_food_marker

@OptIn(ExperimentalNaverMapApi::class)
@Composable
actual fun PinchNaverMap(
    modifier: Modifier,
    position: Position,
    placeList: List<Place>,
    cameraPosition: Position?,
    onCameraStateChange: (CameraState) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState()
    val myLocationInit = remember { mutableStateOf(false) }

    LaunchedEffect(cameraPosition) {
        if(myLocationInit.value) return@LaunchedEffect
        cameraPosition?.let { target ->
            val now = cameraPositionState.position.target
            if (target.latitude != now.latitude || target.longitude != now.longitude) {
                cameraPositionState.animate(CameraUpdate.scrollTo(target.toLatLng()))
            }
            myLocationInit.value = true
        }
    }

    LaunchedEffect(placeList) {
        val filtered = placeList.filter { it.kakaoPlaceId.isNotEmpty() }
        val coords = filtered.map { LatLng(it.latitude, it.longitude) }

        when {
            coords.size >= 2 -> {
                val sw = LatLng(coords.minOf { it.latitude }, coords.minOf { it.longitude })
                val ne = LatLng(coords.maxOf { it.latitude }, coords.maxOf { it.longitude })
                val bounds = LatLngBounds(sw, ne)

                cameraPositionState.animate(
                    CameraUpdate.fitBounds(bounds, 50)
                )
            }
            coords.size == 1 -> {
                cameraPositionState.animate(CameraUpdate.scrollTo(coords.first()))
                cameraPositionState.animate(CameraUpdate.zoomTo(15.0))
            }
            else -> {
                // 마커 없음: 아무 것도 하지 않음
            }
        }
    }



    NaverMap(
        modifier = Modifier
            .height(100.dp)
            .fillMaxWidth(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            minZoom = 3.0,
            locationTrackingMode = LocationTrackingMode.None
        ),
        uiSettings = MapUiSettings(
            isLocationButtonEnabled = false,
            isCompassEnabled = false,
            isZoomControlEnabled = false,
            isScaleBarEnabled = false,
        ),
    ) {

        val lineCoords = placeList.filter { it.kakaoPlaceId.isNotEmpty() }

        if (lineCoords.size >= 2) {
            PolylineOverlay(
                coords = lineCoords.map { LatLng(it.latitude, it.longitude)  },
                width = 1.dp,
                color = Colors.Negative,
                pattern = arrayOf(1.dp, 3.dp)
            )
        }

        lineCoords.forEach {
                MarkerComposable(
                    keys = arrayOf(it.kakaoPlaceId),
                    state = MarkerState(
                        position = LatLng(it.latitude, it.longitude),
                    ),
                    anchor = Offset(0.5f, 0.25f),
                    onClick = { _ ->
                        true
                    }
                ) {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = when (Category.of(it.categoryCode)) {
                                Category.RESTAURANT -> {
                                    painterResource(Res.drawable.ic_food_marker)
                                }

                                else -> {
                                    painterResource(Res.drawable.ic_cafe_marker)
                                }
                            },
                            contentDescription = "marker"
                        )

                        RoundedBox(
                            modifier = Modifier,
                            backgroundColor = Colors.Black_25,
                            cornerRounded = 100
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 2.dp, horizontal = 6.dp)
                                    .widthIn(max = 72.dp),
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