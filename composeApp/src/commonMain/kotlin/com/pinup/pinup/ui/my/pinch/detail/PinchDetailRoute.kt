package com.pinup.pinup.ui.my.pinch.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.ui.main.compose.MainDestination
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.LocationTrackerFactory
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PinchDetailRoute(
    onBackPressed: () -> Unit = {},
    onClickBottomNav: (MainDestination) -> Unit,
    onClickEdit: (Int) -> Unit = {},
) {
    val locationTrackerFactory: LocationTrackerFactory = rememberLocationTrackerFactory(
        accuracy = LocationTrackerAccuracy.Best
    )
    val viewModel: PinchDetailViewModel = koinViewModel(parameters = { parametersOf(locationTrackerFactory.createLocationTracker()) })
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                PinchDetailUiEvent.SuccessModify -> onBackPressed()
            }
        }
    }


    BindLocationTrackerEffect(viewModel.locationTracker)
    PinchWriteScreen(
        title = uiState.value.title,
        description = uiState.value.description,
        createdAt = uiState.value.createdAt,
        pinchList = uiState.value.pinchList,
        position = uiState.value.currentPosition ?: Position.INVALID,
        cameraPosition = uiState.value.cameraPosition,
        onBackPressed = onBackPressed,
        profileUrl = uiState.value.profileUrl,
        onClickBottomNav = onClickBottomNav,
        onClickDelete = viewModel::deletePinch,
        onClickEdit = { onClickEdit(viewModel.pintsId) }
    )
}