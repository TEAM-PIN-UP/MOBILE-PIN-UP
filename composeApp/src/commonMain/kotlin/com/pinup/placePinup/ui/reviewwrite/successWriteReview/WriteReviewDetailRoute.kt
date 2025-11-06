package com.pinup.placePinup.ui.reviewwrite.successWriteReview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WriteReviewDetailRoute(
    viewModel: WriteReviewDetailViewModel = koinViewModel(),
    onBackPressed: () -> Unit = {},
    onClickPlaceDetail: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PinlogDetailScreen(
        pinlogDetail = uiState.pinlogDetail,
        onBackPressed = onBackPressed,
        onClickPlaceDetail = onClickPlaceDetail
    )
}
