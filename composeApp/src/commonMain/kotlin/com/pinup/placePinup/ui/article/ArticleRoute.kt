package com.pinup.placePinup.ui.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.ui.main.compose.MainDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ArticleRoute(
    onClickBottomNav: (MainDestination) -> Unit,
    viewModel: ArticleViewModel = koinViewModel(),
    onClickSearch: () -> Unit = {},
    onClickDetail: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getArticleList()
    }

    ArticleScreen(
        articleList = uiState.pintsListItem,
        profile = uiState.profileUrl,
        getMoreArticle = {},
        onClickBottomNav = onClickBottomNav,
        onClickSearch = onClickSearch,
        onClickDetail = onClickDetail,
        onRefresh = viewModel::refreshView,
        isRefreshing = uiState.isRefreshing
    )
}
