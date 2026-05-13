package com.pinup.placePinup.ui.map
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

import PToastHost
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.main.compose.MainDestination
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import rememberToastState

@Composable
fun MapRoute(
    onBottomMenuClick: (MainDestination) -> Unit,
    onClickEdit: (Int) -> Unit = {},
    onMovePinlogDetail: (Int) -> Unit = {},
    onMoveWriteReview: (String) -> Unit = {},
    onMoveUserProfile: (String) -> Unit = {},
    onClickArticle: (Int) -> Unit = {},
) {
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )
    val mapViewModel: MapViewModel = koinViewModel(parameters = { parametersOf(locationTrackerFactory.createLocationTracker()) })
    val mapUiState = mapViewModel.uiState.collectAsStateWithLifecycle()
    val toast = rememberToastState()
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    var clickedPinlog by remember { mutableStateOf(0) }
    val deletePinlogText = stringResource(Res.string.toast_delete_pinlog)
    val emptyPinlogText = stringResource(Res.string.toast_empty_pinlog)

    LaunchedEffect(Unit) {
        mapViewModel.uiEvent.collectLatest {
            when(it) {
                MapUiEvent.SuccessDelete -> {
                    toast.show(deletePinlogText)
                }
                is MapUiEvent.OnMoveUserProfile -> {
                    onMoveUserProfile(it.name)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        mapViewModel.getMyProfileImage()
    }

    if (isShowDeleteDialog.value) {
        PDialog(
            titleText = stringResource(Res.string.pin_log_delete_dialog_title),
            descriptionText = stringResource(Res.string.pin_log_delete_dialog_description),
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_do_delete),
            onLeftButtonClick = {
                isShowDeleteDialog.value = false
            },
            onRightButtonClick = {
                isShowDeleteDialog.value = false
                mapViewModel.deleteReview(clickedPinlog)
            },
        )
    }

    BindLocationTrackerEffect(mapViewModel.locationTracker)

    if (mapUiState.value.isSearchMode) {
        MapSearchScreen(
            query = mapUiState.value.searchUiState.query,
            places = mapUiState.value.searchUiState.places,
            profileImage = mapUiState.value.profileImage,
            onValueChange = mapViewModel::updateSearchText,
            onClickBack = mapViewModel::updateSearchMode,
            onPlaceClick = { kakaoPlaceId ->
                mapViewModel.getDetailPlace(kakaoPlaceId)
                mapViewModel.updateSearchMode()
            },
            onClickBottomNav = onBottomMenuClick,
            showEmptyToast = { toast.show(emptyPinlogText) }
        )
    } else {
        MapScreen(
            viewModel = mapViewModel,
            searchUiState = mapUiState.value.searchUiState,
            placeDetailUiState = mapUiState.value.placeDetailUiState,
            position = mapUiState.value.currentPosition ?: Position.INVALID,
            cameraPosition = mapUiState.value.cameraPosition,
            isFocusLocation = mapUiState.value.isFocusLocation,
            isShowPinch = mapUiState.value.isShowPinch,
            isCameraMoving = mapUiState.value.isCameraMoving,
            isDetailClicked = mapUiState.value.isDetailClicked,
            pinchUiState = mapUiState.value.pinchUiState,
            profileImage = mapUiState.value.profileImage,
            clearPinchList = mapViewModel::clearPinchDetailList,
            onClickBottomNav = onBottomMenuClick,
            onCameraStateChange = mapViewModel::updateCameraState,
            onChipClick = mapViewModel::updateChipState,
            onPintsChipClick = mapViewModel::updatePintsChipState,
            onPlaceClick = mapViewModel::getDetailPlace,
            onClearDetailPlace = mapViewModel::clearDetailPlace,
            onPinchListClick = mapViewModel::getPinchDetailList,
            onUpdateBookmark = mapViewModel::updateBookmark,
            onUpdateSortType = mapViewModel::updateSortType,
            onUpdatePosition = mapViewModel::collectPosition,
            onUpdateShowBookmarks = mapViewModel::updateShowPinch,
            onUpdateFocusLocation = mapViewModel::updateFocusLocation,
            onClickSearch = mapViewModel::updateSearchMode,
            consumeDetailClicked = mapViewModel::consumedDetailClicked,
            onClickGetPlace = mapViewModel::getPlaces,
            onClickEdit = onClickEdit,
            onClickDelete = {
                clickedPinlog = it
                isShowDeleteDialog.value = true
            },
            onMovePinlogDetail = onMovePinlogDetail,
            onClickLike = mapViewModel::likeChanged,
            onMoveWriteReview = onMoveWriteReview,
            onMoveUserProfile = mapViewModel::onProfileClick,
            onClickArticle = onClickArticle,
        )
    }

    PToastHost(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 24.dp, start = 20.dp, end = 20.dp),
        state = toast
    )
}