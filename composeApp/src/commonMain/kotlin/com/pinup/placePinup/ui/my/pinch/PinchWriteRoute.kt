package com.pinup.placePinup.ui.my.pinch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.theme.Texts
import com.pinup.placePinup.util.Const
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PinchWriteRoute(
    navHostController: NavHostController,
    onBackPressed: () -> Unit = {},
    onMoveWriteReview: (Place) -> Unit = {},
    onMovePintsDetail: (Int) -> Unit = {},
) {
    val navBackStackEntry = remember { navHostController.currentBackStackEntry }
    val savedStateHandle = navBackStackEntry?.savedStateHandle
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )
    val viewModel: PinchWriteViewModel = koinViewModel(parameters = { parametersOf(locationTrackerFactory.createLocationTracker()) })
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isShowCompleteDialog = remember { mutableStateOf(false to 0) }
    val isShowWritePinlogDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        savedStateHandle?.getStateFlow(Const.NavKey.PINLOG_WRITE_RESULT, false)?.collect { result ->
            if (result) {
                savedStateHandle[Const.NavKey.PINLOG_WRITE_RESULT] = false
                viewModel.updatePlace(viewModel.selectPlace, viewModel.selectIndex)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                is PinchWriteUiEvent.SuccessModify -> {
                    isShowCompleteDialog.value = true to it.id
                }
            }
        }
    }

    BindLocationTrackerEffect(viewModel.locationTracker)
    PinchWriteScreen(
        title = uiState.value.title,
        description = uiState.value.description,
        createdAt = uiState.value.createdAt,
        pinchList = uiState.value.pinchList,
        searchedList = uiState.value.searchedList,
        position = uiState.value.currentPosition ?: Position.INVALID,
        cameraPosition = uiState.value.cameraPosition,
        onBackPressed = onBackPressed,
        onTitleChanged = viewModel::updateTitle,
        onDescriptionChanged = viewModel::updateDescription,
        onNameChanged = viewModel::updatePinchName,
        moveItem = viewModel::moveItem,
        onClickDelete = viewModel::deletePinch,
        onPlaceClick = viewModel::updatePlace,
        onClickShowDialog = { place, index ->
            isShowWritePinlogDialog.value = true
            viewModel.selectPlace = place
            viewModel.selectIndex = index
        },
        registerPints = viewModel::registerPints
    )

    if (isShowCompleteDialog.value.first) {
        PDialog(
            titleText = Texts.Pinch.PINTS_WRITE_COMPLETE_DIALOG_TITLE,
            descriptionText = Texts.Pinch.PINTS_WRITE_COMPLETE_DIALOG_CONTENT,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Word.DO_CONFiRM,
            onLeftButtonClick = {
                isShowCompleteDialog.value = false to isShowCompleteDialog.value.second
                onBackPressed()
            },
            onRightButtonClick = {
                isShowCompleteDialog.value = false to isShowCompleteDialog.value.second
                onMovePintsDetail(isShowCompleteDialog.value.second)
            },
        )
    }

    if (isShowWritePinlogDialog.value) {
        PDialog(
            titleText = Texts.Pinch.NO_PINLOG_DIALOG_TITLE,
            descriptionText = Texts.Pinch.NO_PINLOG_DIALOG_CONTENT,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Word.DO_REGISTER,
            onLeftButtonClick = {
                isShowWritePinlogDialog.value = false
            },
            onRightButtonClick = {
                isShowWritePinlogDialog.value = false
                onMoveWriteReview(viewModel.selectPlace)
            },
        )
    }
}