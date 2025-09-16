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
    viewModel: ArticleViewModel = koinViewModel(),
    onClickPlaceDetail: (String) -> Unit = {},
    onclickBack: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getArticleList()
    }

    ArticleDetailScreen(
        detail = ArticleDetail(),
        onClickPlaceDetail = onClickPlaceDetail,
        onClickScrap = {},
        onClickBack = onclickBack
    )
}
