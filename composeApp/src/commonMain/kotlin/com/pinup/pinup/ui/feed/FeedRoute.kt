package com.pinup.pinup.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.main.compose.MainDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(
    onClickBottomNav: (MainDestination) -> Unit,
    viewModel: FeedViewModel = koinViewModel(),
    onClickEdit: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    FeedScreen(
        reviewList = uiState.pagingReview.reviews,
        onClickBottomNav = onClickBottomNav,
        onClickSearch = {},
        onClickEdit = {
            onClickEdit(it)
        },
        onClickDelete = viewModel::deleteReview,
    )
}
