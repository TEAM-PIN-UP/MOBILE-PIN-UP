package com.pinup.pinup.ui.my.pinch

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.main.compose.MainDestination
import kotlinx.collections.immutable.toPersistentList
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PinchWriteRoute(
    viewModel: PinchWriteViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    PinchWriteScreen(
        title = uiState.value.title,
        description = uiState.value.description,
        createdAt = uiState.value.createdAt,
        pinchList = uiState.value.pinchList,
        searchedList = uiState.value.searchedList,
        onBackPressed = onBackPressed,
        onTitleChanged = viewModel::updateTitle,
        onDescriptionChanged = viewModel::updateDescription,
        onNameChanged = viewModel::updatePinchName,
        moveItem = viewModel::moveItem,
        onClickDelete = viewModel::deletePinch,
        onPlaceClick = viewModel::updatePlace
    )
}