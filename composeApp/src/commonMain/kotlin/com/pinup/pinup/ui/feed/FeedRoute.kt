package com.pinup.pinup.ui.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.component.PDialog
import com.pinup.pinup.ui.feed.search.FeedSearchScreen
import com.pinup.pinup.ui.main.compose.MainDestination
import com.pinup.pinup.ui.theme.Texts
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(
    onClickBottomNav: (MainDestination) -> Unit,
    viewModel: FeedViewModel = koinViewModel(),
    onClickEdit: (Int) -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val isClickedFeedId = remember { mutableStateOf(-1) }

    LaunchedEffect(Unit) {
        viewModel.getFeedList()
    }

    if (isShowDeleteDialog.value) {
        PDialog(
            titleText = Texts.PinLog.DELETE_DIALOG_TITLE,
            descriptionText = Texts.PinLog.DELETE_DIALOG_DESCRIPTION,
            leftButtonText = Texts.Word.DO_RETURN,
            rightButtonText = Texts.Word.DO_DELETE,
            onLeftButtonClick = {
                isShowDeleteDialog.value = false
            },
            onRightButtonClick = {
                isShowDeleteDialog.value = false
                viewModel.deleteReview(isClickedFeedId.value)
            },
        )
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
            onClickDelete = {
                isClickedFeedId.value = it
                isShowDeleteDialog.value = true
            },
            onClickDetail = onClickDetail,
            onClickLike = viewModel::likeChanged
        )
    }
}
