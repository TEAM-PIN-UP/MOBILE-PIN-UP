package com.pinup.pinup.ui.pinlogDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PinlogDetailRoute(
    viewModel: PinlogDetailViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                PinlogUiEvent.MoveBack -> onBackPressed()
            }
        }
    }

    PinlogDetailScreen(
        reviewId = viewModel.reviewId,
        onBackPressed = onBackPressed,
        query = uiState.myComment,
        onValueChange = viewModel::updateMyComment,
        onClickEdit = {},
        onClickDelete = viewModel::deleteReview
    )
}
