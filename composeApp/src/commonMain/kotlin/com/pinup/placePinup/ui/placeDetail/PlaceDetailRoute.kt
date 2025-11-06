package com.pinup.placePinup.ui.placeDetail

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PlaceDetailRoute(
    viewModel: PlaceDetailViewModel = koinViewModel(),
    onClickBack: () -> Unit = {},
    onClickEdit: (Int) -> Unit = {},
    onMovePinlogDetail: (Int) -> Unit = {},
    onMoveWriteReview: (String) -> Unit = {},
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    PlaceDetailScreen(
        detailPlace = uiState.value.detailPlace,
        onMoveWriteReview = onMoveWriteReview,
        onBackPressed = onClickBack,
        onUpdateBookmark = viewModel::updateBookmark,
        onClickEdit = onClickEdit,
        onClickDelete = viewModel::deleteReview,
        onClickLike = viewModel::likeChanged,
        onMovePinlogDetail = onMovePinlogDetail
    )
}