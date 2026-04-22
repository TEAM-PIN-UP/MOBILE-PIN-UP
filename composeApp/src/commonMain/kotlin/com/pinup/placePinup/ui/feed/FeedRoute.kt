package com.pinup.placePinup.ui.feed
import pinup.composeapp.generated.resources.Res
import pinup.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.ui.component.PDialog
import com.pinup.placePinup.ui.feed.search.FeedSearchScreen
import com.pinup.placePinup.ui.main.compose.MainDestination
import com.pinup.placePinup.ui.theme.Texts
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedRoute(
    onClickBottomNav: (MainDestination) -> Unit,
    viewModel: FeedViewModel = koinViewModel(),
    onClickEdit: (Int) -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
    onMoveUserProfile: (String) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isShowDeleteDialog = remember { mutableStateOf(false) }
    val isClickedFeedId = remember { mutableStateOf(-1) }

    LaunchedEffect(Unit) {
        viewModel.getFeedList()
        viewModel.getMyProfile()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                is FeedUiEvent.OnMoveUserProfile -> {
                    onMoveUserProfile(it.name)
                }
            }
        }
    }

    if (isShowDeleteDialog.value) {
        PDialog(
            titleText = Texts.PinLog.DELETE_DIALOG_TITLE,
            descriptionText = Texts.PinLog.DELETE_DIALOG_DESCRIPTION,
            leftButtonText = stringResource(Res.string.word_do_return),
            rightButtonText = stringResource(Res.string.word_do_delete),
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
            reviewList = uiState.searchedPagingReview.reviews,
            recentSearchList = uiState.recentSearchList,
            onValueChange = viewModel::updateSearchText,
            onClickSearch = viewModel::onClickSearch,
            onClickDeleteRecentSearch = viewModel::deleteRecentSearch,
            onClickBack = viewModel::updateSearchMode,
            onClickBottomNav = onClickBottomNav,
            getMoreFeed = viewModel::getMoreFeed,
            onClickEdit = {
                onClickEdit(it)
            },
            onClickDelete = viewModel::deleteReview,
            onClickDetail = onClickDetail,
            onClickLike = viewModel::likeChanged,
            onMoveUserProfile = viewModel::onProfileClick
        )
    } else {
        FeedScreen(
            reviewList = uiState.pagingReview.reviews,
            profile = uiState.profileUrl,
            isRefreshing = uiState.isRefreshing,
            getMoreFeed = viewModel::getMoreFeed,
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
            onClickLike = viewModel::likeChanged,
            onRefresh = viewModel::refreshView,
            onMoveUserProfile = viewModel::onProfileClick
        )
    }
}
