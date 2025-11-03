package com.pinup.pinup.ui.article.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.domain.model.ArticleDetail
import com.pinup.pinup.ui.main.compose.MainDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ArticleDetailRoute(
    viewModel: ArticleDetailViewModel = koinViewModel(),
    onClickPlaceDetail: (String) -> Unit = {},
    onClickBack: () -> Unit = {},
    onClickArticle: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ArticleDetailScreen(
        editorPintsDetail = uiState.editorPintsDetail,
        articleList = uiState.pintsListItem,
        onClickPlaceDetail = onClickPlaceDetail,
        onClickScrap = viewModel::updateBookmark,
        onClickBack = onClickBack,
        onClickArticle = onClickArticle
    )
}
