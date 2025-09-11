package com.pinup.pinup.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.ui.feed.search.FeedSearchScreen
import com.pinup.pinup.ui.main.compose.MainDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(
    onClickBottomNav: (MainDestination) -> Unit,
    viewModel: FeedViewModel = koinViewModel(),
    onClickEdit: (Int) -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getFeedList()
    }

    if (uiState.searchMode) {
        FeedSearchScreen(
            query = uiState.searchText,
            profile = uiState.profileUrl,
            reviewList = uiState.pagingReview.reviews,
            recentSearchList = uiState.recentSearchList,
            onValueChange = viewModel::updateSearchText,
            onClickSearch = viewModel::onClickSearch,
            onClickDeleteRecentSearch = viewModel::deleteRecentSearch,
            onClickBack = viewModel::updateSearchMode,
            onClickBottomNav = onClickBottomNav,
            getMoreFeed = viewModel::getFeedList,
            onClickEdit = {
                onClickEdit(it)
            },
            onClickDelete = viewModel::deleteReview,
            onClickDetail = onClickDetail,
            onClickLike = viewModel::likeChanged,
        )
    } else {
        FeedScreen(
            reviewList = uiState.pagingReview.reviews,
            profile = uiState.profileUrl,
            getMoreFeed = viewModel::getFeedList,
            onClickBottomNav = onClickBottomNav,
            onClickSearch = viewModel::updateSearchMode,
            onClickEdit = {
                onClickEdit(it)
            },
            onClickDelete = viewModel::deleteReview,
            onClickDetail = onClickDetail,
            onClickLike = viewModel::likeChanged
        )
    }
}
