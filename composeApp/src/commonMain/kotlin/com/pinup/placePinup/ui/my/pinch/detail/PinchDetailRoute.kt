package com.pinup.placePinup.ui.my.pinch.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.domain.model.Position
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PinchDetailRoute(
    onBackPressed: () -> Unit = {},
    onClickEdit: (Int) -> Unit = {},
) {
    val viewModel: PinchDetailViewModel = koinViewModel()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                PinchDetailUiEvent.SuccessModify -> onBackPressed()
            }
        }
    }

    PinchDetailScreen(
        title = uiState.value.title,
        description = uiState.value.description,
        createdAt = uiState.value.createdAt,
        pinchList = uiState.value.pinchList,
        position = uiState.value.currentPosition ?: Position.INVALID,
        cameraPosition = uiState.value.cameraPosition,
        onBackPressed = onBackPressed,
        onClickDelete = viewModel::deletePinch,
        onClickEdit = { onClickEdit(viewModel.pintsId) },
        getPintsDetail = viewModel::getPintsDetail
    )
}