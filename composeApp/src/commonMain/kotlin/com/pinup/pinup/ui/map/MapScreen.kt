package com.pinup.pinup.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
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
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.extentions.clickableSingleWithNoRipple
import com.pinup.pinup.extentions.clickableWithNoRipple
import com.pinup.pinup.platform.PlatformNaverMap
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.component.BottomBar
import com.pinup.pinup.ui.component.PBottomSheet
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.component.SortBottomSheet
import com.pinup.pinup.ui.main.compose.MainDestination
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
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.ic_focus
import pinup.composeapp.generated.resources.ic_pinch_off
import pinup.composeapp.generated.resources.ic_pinch_on

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isFocusLocation: Boolean,
    isShowPinch: Boolean,
    isCameraMoving: Boolean,
    isDetailClicked: Boolean,
    pinchList: List<PinchListItem> = emptyList(),
    pinchDetailList: List<ReviewedPlace> = emptyList(),
    profileImage: String,
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
    onFocusChange: (Boolean) -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    var parentHeightPx by remember { mutableIntStateOf(0) }
    val parentHeightDp = with(LocalDensity.current) { parentHeightPx.toDp() }
    var bottomBarHeightPx by remember { mutableIntStateOf(0) }
    val bottomBarHeightDp = with(LocalDensity.current) { bottomBarHeightPx.toDp() }
    val statusBarHeightDp = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()
    val expandedHeight by remember(parentHeightDp) {
        derivedStateOf {
            parentHeightDp - 24.dp - bottomBarHeightDp - statusBarHeightDp
        }
    }
    val halfHeight by remember(parentHeightDp) {
        derivedStateOf {
            (parentHeightDp - bottomBarHeightDp) / 2
        }
    }
    val hiddenHeight by remember(parentHeightDp) {
        derivedStateOf {
            35.dp
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
            when (permissionsController.getPermissionState(Permission.LOCATION)) {
                PermissionState.Granted -> {
                    hLog("현위치 >>> ${position}")
                    if (isFocusLocation) {
                        hLog("내위치 포커싱 끄기")
                    } else {
                        hLog("내위치 포커싱 켜기")
                    }
                    onUpdateFocusLocation(isFocusLocation.not())
                }
                PermissionState.Denied -> {
                    hLog("위치 권한 거부 상태로 재요청")
                    requestPermission()
                }
                else -> {
                    hLog("위치 권한 거부 상태로 직접 설정 필요")
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

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            SortBottomSheet(
                selectedSortType = searchUiState.sortType,
                allPermissionsGranted = isPermissionGranted.value,
                onSortTypeSelect = {
                    scope.launch {
                        sheetState.hide()
                        onUpdateSortType(it)
                    }
                }
            )
        },
        sheetBackgroundColor = Colors.White,
        sheetState = sheetState,
    ) {
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
                pinchDetailList = pinchDetailList,
                isShowBookmarks = isShowPinch,
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
                    // TODO 만약 핀츠가 없다면 해당 컬럼 안보이게
                    Card(
                        modifier = Modifier
                            .clickableWithNoRipple {
                                onUpdateShowBookmarks()
                            },
                        shape = CircleShape,
                        backgroundColor = Colors.White,
                        elevation = 1.dp
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(10.dp)
                                .size(20.dp),
                            painter = if (isShowPinch) {
                                painterResource(Res.drawable.ic_pinch_on)
                            } else {
                                painterResource(Res.drawable.ic_pinch_off)
                            },
                            contentDescription = "pinch filter"
                        )
                    }

                    Card(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .clickableSingleWithNoRipple {
                                getCurrentLocation()
                            },
                        shape = CircleShape,
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(sheet) {
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    PBottomSheet(
                        expandedHeight = expandedHeight,
                        halfHeight = halfHeight,
                        hiddenHeight = hiddenHeight,
                        onSheetHeightChanged = {
                            bottomSheetHeight = it
                        },
                        isMoving = isCameraMoving,
                        isDetailClicked = isDetailClicked,
                        isFocusSearch = searchUiState.isFocus
                    ) {
                        MapBottomSheetNavHost(
                            searchUiState = searchUiState,
                            placeDetailUiState = placeDetailUiState,
                            isScrollable = alpha == 0f,
                            onValueChange = onValueChange,
                            onChipClick = onChipClick,
                            isShowPinch = isShowPinch,
                            onPlaceClick = {
                                onPlaceClick(it.kakaoPlaceId)
                            },
                            onClearDetailPlace = onClearDetailPlace,
                            onUpdateBookmark = onUpdateBookmark,
                            onSelectSortTypeClick = { scope.launch { sheetState.show() } },
                            onFocusChange = onFocusChange
                        )
                    }

                    Box(
                        modifier = Modifier
                            .onSizeChanged { bottomBarHeightPx = it.height }
                    ){
                        BottomBar(
                            selectedMenu = MainDestination.Map,
                            profileImage = "",
                            onBottomMenuClick = onClickBottomNav
                        )
                    }
                }
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