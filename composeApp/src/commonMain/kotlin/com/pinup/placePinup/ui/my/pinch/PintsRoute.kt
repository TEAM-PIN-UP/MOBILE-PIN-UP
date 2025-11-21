package com.pinup.placePinup.ui.my.pinch

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PintsRoute(
    viewModel: PintsViewModel = koinViewModel(),
    onMovePintsDetail: (Int) -> Unit = {},
    onBackPressed: () -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    PintsScreen(
        pintsList = uiState.value.pintsPageAble.content,
        onBackPressed = onBackPressed,
        onMovePintsDetail = onMovePintsDetail,
        getMorePints = viewModel::getMorePints
    )
}