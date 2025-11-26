package com.pinup.placePinup.ui.map

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.pinup.placePinup.domain.model.CameraState
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.extentions.clickableSingleWithNoRipple
import com.pinup.placePinup.extentions.clickableWithNoRipple
import com.pinup.placePinup.platform.PlatformNaverMap
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.ui.component.BottomBar
import com.pinup.placePinup.ui.component.PBottomSheet
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.component.PinlogMenuBottomSheet
import com.pinup.placePinup.ui.component.RoundedBox
import com.pinup.placePinup.ui.component.SortBottomSheet
import com.pinup.placePinup.ui.main.compose.MainDestination
import com.pinup.placePinup.ui.model.ChipState
import com.pinup.placePinup.ui.theme.Colors
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.ui.theme.Typography
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
import pinup.composeapp.generated.resources.ic_rotate

@Composable
fun MapScreen(
    viewModel: MapViewModel,
    searchUiState: SearchUiState,
    placeDetailUiState: PlaceDetailUiState,
    isFocusLocation: Boolean,
    isShowPinch: Boolean,
    isCameraMoving: Boolean,
    isDetailClicked: Boolean,
    pinchUiState: PinchUiState,
    clearPinchList: () -> Unit = {},
    position: Position = Position.INVALID,
    cameraPosition: Position? = null,
    profileImage: String = "",
    onCameraStateChange: (CameraState) -> Unit = { },
    onValueChange: (String) -> Unit = {},
    onChipClick: (ChipState) -> Unit = {},
    onPintsChipClick: (ChipState) -> Unit = {},
    onPlaceClick: (String) -> Unit = { },
    onClearDetailPlace: () -> Unit = {},
    onPinchListClick: (Int) -> Unit = {},
    onUpdateBookmark: (String, Boolean) -> Unit = { _, _ -> },
    onUpdateSortType: (SortType) -> Unit = {},
    onUpdatePosition: () -> Unit = {},
    onUpdateShowBookmarks: () -> Unit = {},
    onUpdateFocusLocation: (Boolean) -> Unit = {},
    onFocusChange: (Boolean) -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    consumeDetailClicked: () -> Unit = {},
    onClickGetPlace: () -> Unit = {},
    onClickEdit: (Int) -> Unit = {},
    onClickDelete: (Int) -> Unit = {},
    onClickLike: (Int, Boolean) -> Unit = {_, _ -> },
    onMovePinlogDetail: (Int) -> Unit = {},
    onMoveWriteReview: (String) -> Unit = {},
    onMoveUserProfile: (String) -> Unit = {},
    onClickArticle: (Int) -> Unit = {},
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )
    var isPinlogSheet by remember { mutableStateOf(false) }
    var clickedPinlogId by remember { mutableIntStateOf(0) }

    var parentHeightPx by remember { mutableIntStateOf(0) }
    val parentHeightDp = with(LocalDensity.current) { parentHeightPx.toDp() }
    var bottomBarHeightDp by remember { mutableStateOf(0.dp) }
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
    val isMapClicked = remember { mutableStateOf(false) }

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

            if (isPermissionGranted.value) {
                onUpdatePosition()
            }
        }

        onPauseOrDispose { }
    }

    DisposableEffect(Unit){
        onDispose {
            viewModel.clearDetailPlace()
            viewModel.clearPinchDetailList()
        }
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            if (isPinlogSheet) {
                PinlogMenuBottomSheet(
                    onClickEdit = {
                        onClickEdit(clickedPinlogId)
                        scope.launch { sheetState.hide() }
                    },
                    onClickDelete = {
                        onClickDelete(clickedPinlogId)
                        scope.launch { sheetState.hide() }
                    },
                )
            } else {
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
            }
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
                pinchUiState = pinchUiState,
                isShowPinch = isShowPinch,
                cameraPosition = cameraPosition,
                onPlaceClick = onPlaceClick,
                onCameraStateChange = onCameraStateChange,
                onMapClick = { isMapClicked.value = true }
            )

            ConstraintLayout(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                val (text, button, sheet) = createRefs()
                RoundedBox(
                    modifier = Modifier
                        .alpha(alpha)
                        .padding(bottom = 14.dp)
                        .constrainAs(text) {
                            bottom.linkTo(sheet.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    cornerRounded = 999
                ) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                onClickGetPlace()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ic_rotate),
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = Texts.PinMap.SEARCH_BUTTON,
                            color = Colors.Gray800,
                            style = Typography.B3.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

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
                        .padding(bottom = bottomBarHeightDp)
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
                        consumeDetailClicked = consumeDetailClicked,
                        isMoving = isCameraMoving,
                        isDetailClicked = isDetailClicked,
                        isFocusSearch = searchUiState.isFocus,
                        isShowPinch = isShowPinch,
                        isMapClicked = isMapClicked.value,
                        consumeMapClicked = { isMapClicked.value = false },
                    ) {
                        MapBottomSheetNavHost(
                            searchUiState = searchUiState,
                            placeDetailUiState = placeDetailUiState,
                            pinchUiState = pinchUiState,
                            isScrollable = alpha == 0f,
                            onValueChange = onValueChange,
                            onChipClick = onChipClick,
                            onPintsChipClick = onPintsChipClick,
                            isShowPinch = isShowPinch,
                            clearPinchList = clearPinchList,
                            onPlaceClick = onPlaceClick,
                            onPinchListClick = onPinchListClick,
                            onClearDetailPlace = onClearDetailPlace,
                            onUpdateBookmark = onUpdateBookmark,
                            onSelectSortTypeClick = { scope.launch {
                                isPinlogSheet = false
                                sheetState.show()
                            } },
                            onClickMenu = { scope.launch {
                                isPinlogSheet = true
                                clickedPinlogId = it
                                sheetState.show()
                            } },
                            onFocusChange = onFocusChange,
                            onClickLike = onClickLike,
                            onMovePinlogDetail = onMovePinlogDetail,
                            onMoveWriteReview = onMoveWriteReview,
                            onMoveUserProfile = onMoveUserProfile,
                            onClickArticle = onClickArticle
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            BottomBar(
                selectedMenu = MainDestination.Map,
                profileImage = profileImage,
                onBottomMenuClick = onClickBottomNav,
                onSizeChanged = { bottomBarHeightDp = it }
            )
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