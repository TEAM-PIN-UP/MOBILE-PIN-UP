package com.pinup.pinup.ui.placeDetail

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.main.compose.MainDestination
import kotlinx.collections.immutable.toPersistentList
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