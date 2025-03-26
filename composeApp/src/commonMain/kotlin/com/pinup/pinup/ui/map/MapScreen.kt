package com.pinup.pinup.ui.map

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LifecycleResumeEffect
import coil3.compose.LocalPlatformContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.compose.CameraPositionState
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
import com.pinup.pinup.PlatformNaverMap
import com.pinup.pinup.R
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.hLog
import com.pinup.pinup.ui.component.PBottomSheet
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.component.RoundedBox
import com.pinup.pinup.ui.model.ChipState
import com.pinup.pinup.ui.theme.Colors
import com.pinup.pinup.ui.theme.Typography
import kotlinx.coroutines.launch

@Composable
fun MapScreen(
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isFocusLocation: Boolean,
    isShowBookmarks: Boolean,
    position: Position = Position.INVALID,
    cameraPosition: Position? = null,
    onCameraStateChange: (CameraPositionState) -> Unit = { },
    onValueChange: (String) -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (String) -> Unit = { },
    onClearDetailPlace: () -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
    onUpdateSortType: (SortType) -> Unit = {},
    onUpdatePosition: () -> Unit = {},
    onUpdateShowBookmarks: () -> Unit = {},
    onUpdateFocusLocation: (Boolean) -> Unit = {},
) {
    val context = LocalPlatformContext.current
    var parentHeightPx by remember { mutableIntStateOf(0) }
    val parentHeightDp = with(LocalDensity.current) { parentHeightPx.toDp() }
    val expandedHeight by remember(parentHeightDp) {
        derivedStateOf {
            parentHeightDp - 28.dp
        }
    }
    val halfHeight by remember(parentHeightDp) {
        derivedStateOf {
            parentHeightDp / 2
        }
    }
    val hiddenHeight by remember(parentHeightDp) {
        derivedStateOf {
            parentHeightDp / 4
        }
    }
    var bottomSheetHeight by remember { mutableStateOf(halfHeight) }
    val alpha by remember(bottomSheetHeight) {
        derivedStateOf {
            ((expandedHeight - bottomSheetHeight) / (parentHeightDp * 1 / 4)).coerceIn(0f, 1f)
        }
    }
    val isShowPermissionDialog = remember { mutableStateOf(false) }

    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        hLog("permissionsMap >>> ${permissionsMap.values}")
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            onUpdatePosition()
        }
    }

    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    fun requestPermission() {
        launcherMultiplePermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    fun getCurrentLocation() {
        when (permissionState.status) {
            is PermissionStatus.Denied -> {
                hLog( "shouldShowRationale >>> ${permissionState.status.shouldShowRationale}")
                if (permissionState.status.shouldShowRationale) {
                    requestPermission()
                } else {
                    isShowPermissionDialog.value = true
                }
            }
            PermissionStatus.Granted -> {
                hLog( "allRequiredPermission >>> ${position}")
                hLog("2")
                onUpdateFocusLocation(isFocusLocation.not())
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        hLog( "allRequiredPermission >>> ${permissionState.status.isGranted}")
        hLog( "shouldShowRationale >>> ${permissionState.status.shouldShowRationale}")
        if (permissionState.status.isGranted) {
            onUpdatePosition()
        } else if (permissionState.status.shouldShowRationale) {
            requestPermission()
        }

        onPauseOrDispose { }
    }

    Box(
        modifier = Modifier
            .onSizeChanged {
                parentHeightPx = it.height
            }
    ) {
        PlatformNaverMap(
            modifier = Modifier,
            position = position,
            searchUiState = searchUiState,
            placeDetailUiState = placeDetailUiState,
            isShowBookmarks = isShowBookmarks,
            cameraPosition = cameraPosition,
            onPlaceClick = onPlaceClick,
        )

        ConstraintLayout(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            val (button, sheet) = createRefs()
            Column(
                modifier = Modifier
                    .alpha(alpha)
                    .padding(bottom = 16.dp, end = 16.dp)
                    .constrainAs(button) {
                        bottom.linkTo(sheet.top)
                        end.linkTo(parent.end)
                    },
            ) {
                Card(
                    modifier = Modifier
                        .clickableWithNoRipple {
                            onUpdateShowBookmarks()
                        },
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = Colors.White,
                    elevation = 1.dp
                ) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .size(20.dp),
                        painter = if (isShowBookmarks) {
                            painterResource(Res.drawable.ic_bookmark_on)
                        } else {
                            painterResource(Res.drawable.ic_bookmark_off)
                        },
                        contentDescription = "bookmark filter"
                    )
                }

                Card(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clickableSingleWithNoRipple {
                            getCurrentLocation()
                        },
                    shape = RoundedCornerShape(8.dp),
                    backgroundColor = Colors.White,
                    elevation = 1.dp
                ) {
                    Image(
                        modifier = Modifier
                            .padding(10.dp),
                        painter = painterResource(Res.drawable.ic_focus),
                        contentDescription = "focusing",
                        colorFilter = if (isFocusLocation) {
                            ColorFilter.tint(Colors.Error)
                        } else {
                            null
                        }
                    )
                }
            }

            PBottomSheet(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(sheet) {
                        bottom.linkTo(parent.bottom)
                    },
                expandedHeight = expandedHeight,
                halfHeight = halfHeight,
                hiddenHeight = hiddenHeight,
                onSheetHeightChanged = {
                    bottomSheetHeight = it
                },
            ) {
                MapBottomSheetNavHost(
                    searchUiState = searchUiState,
                    placeDetailUiState = placeDetailUiState,
                    allPermissionsGranted = permissionState.status.isGranted,
                    isScrollable = alpha == 0f,
                    onValueChange = onValueChange,
                    onChipClick = onChipClick,
                    onPlaceClick = {
                        onPlaceClick(it.kakaoPlaceId)
                    },
                    onClearDetailPlace = onClearDetailPlace,
                    onUpdateBookmark = onUpdateBookmark,
                    onUpdateSortType = onUpdateSortType
                )
            }
        }
    }

    if (isShowPermissionDialog.value) {
        PDialog(
            titleText = "권한 필요",
            descriptionText = "위치 권한 허용이 필요해요.\n확인을 누르시면 설정 화면으로 이동합니다",
            leftButtonText = "취소",
            rightButtonText = "확인",
            onLeftButtonClick = {
                isShowPermissionDialog.value = false
            },
            onRightButtonClick = {
                isShowPermissionDialog.value = false
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.parse("package:" + context.packageName)
                }
                context.startActivity(intent)
            },
        )
    }
}