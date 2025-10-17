package com.pinup.pinup.ui.my.pinch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.map.MapUiEvent
import com.pinup.pinup.ui.theme.Texts
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PinchWriteRoute(
    onBackPressed: () -> Unit = {},
    onMoveWriteReview: (String) -> Unit = {},
) {
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )
    val viewModel: PinchWriteViewModel = koinViewModel(parameters = { parametersOf(locationTrackerFactory.createLocationTracker()) })
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                PinchWriteUiEvent.SuccessModify -> onBackPressed()
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
        onMoveWriteReview = onMoveWriteReview,
        registerPints = viewModel::registerPints
    )
}