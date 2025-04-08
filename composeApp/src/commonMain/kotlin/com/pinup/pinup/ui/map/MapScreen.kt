package com.pinup.pinup.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.platform.PlatformNaverMap
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.component.PBottomSheet
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.model.ChipState
import com.pinup.pinup.ui.theme.Colors
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.location.BACKGROUND_LOCATION
import dev.icerock.moko.permissions.location.COARSE_LOCATION
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_bookmark_off
import pinup.composeapp.generated.resources.ic_bookmark_on
import pinup.composeapp.generated.resources.ic_focus

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isFocusLocation: Boolean,
    isShowBookmarks: Boolean,
    position: Position = Position.INVALID,
    cameraPosition: Position? = null,
    onCameraStateChange: (CameraState) -> Unit = { },
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
    val scope: CoroutineScope = rememberCoroutineScope()
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
    var bottomSheetHeight by remember { mutableStateOf(hiddenHeight) }
    val alpha by remember(parentHeightDp) {
        derivedStateOf {
            ((expandedHeight - bottomSheetHeight) / (parentHeightDp * 1 / 4)).coerceIn(0f, 1f)
        }
    }
    val isShowPermissionDialog = remember { mutableStateOf(false) }

    val permissionFactory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController: PermissionsController = remember(permissionFactory) {
        permissionFactory.createPermissionsController()
    }

    BindEffect(permissionsController)
    val isPermissionGranted = remember { mutableStateOf(false) }

    fun requestPermission() {
        scope.launch {
            try {
                permissionsController.providePermission(Permission.LOCATION)
            } catch (exc: RequestCanceledException) {
                hLog("RequestCanceledException")
            } catch (exc: DeniedException) {
                hLog("DeniedException")
                isShowPermissionDialog.value = true
            } catch (exc: DeniedAlwaysException) {
                hLog("DeniedAlwaysException")
                isShowPermissionDialog.value = true
            }
        }
    }

    fun getCurrentLocation() {
        scope.launch {
            hLog("1 >>> ${permissionsController.isPermissionGranted(Permission.LOCATION)}")
            hLog("2 >>> ${permissionsController.isPermissionGranted(Permission.COARSE_LOCATION)}")
            hLog("3 >>> ${permissionsController.isPermissionGranted(Permission.BACKGROUND_LOCATION)}")
            when (permissionsController.getPermissionState(Permission.LOCATION)) {
                PermissionState.Granted -> {
                    hLog("allRequiredPermission >>> ${position}")
                    onUpdateFocusLocation(isFocusLocation.not())
                }
                PermissionState.Denied -> {
                    requestPermission()
                }
                else -> {
                    isShowPermissionDialog.value = true
                }
            }
        }
    }

    LaunchedEffect(permissionsController) {
        isPermissionGranted.value = permissionsController.isPermissionGranted(Permission.LOCATION)
        hLog("처음 권한 확인 >>> ${isPermissionGranted.value}")
        if (isPermissionGranted.value.not()) {
            hLog("권한 요청")
            try {
                permissionsController.providePermission(Permission.LOCATION)
            } catch (exc: RequestCanceledException) {
                hLog("RequestCanceledException")
            } catch (exc: DeniedException) {
                hLog("DeniedException")
            } catch (exc: DeniedAlwaysException) {
                hLog("DeniedAlwaysException")
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        scope.launch {
            isPermissionGranted.value = permissionsController.isPermissionGranted(Permission.LOCATION)
            hLog("권한 수정하고 앱으로 돌아옴 >>> ${isPermissionGranted.value}")
            if (isPermissionGranted.value) {
                onUpdatePosition()
            }
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
            viewModel = viewModel,
            position = position,
            searchUiState = searchUiState,
            placeDetailUiState = placeDetailUiState,
            isShowBookmarks = isShowBookmarks,
            cameraPosition = cameraPosition,
            onPlaceClick = onPlaceClick,
            onCameraStateChange = onCameraStateChange
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
                        .clickableWithNoRipple {
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
                    allPermissionsGranted = isPermissionGranted.value,
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
                permissionsController.openAppSettings()
            },
        )
    }
}