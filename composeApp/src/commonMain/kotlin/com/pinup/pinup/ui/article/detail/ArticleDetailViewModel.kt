package com.pinup.pinup.ui.article.detail

import com.pinup.pinup.domain.model.ArticleDetail
import com.pinup.pinup.domain.model.ArticlePlace
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState

class ArticleDetailViewModel(
) : BaseViewModel<ArticleDetailUiState, UiEvent>(ArticleDetailUiState()) {



}

data class ArticleDetailUiState(
    val articleList: List<ArticlePlace> = emptyList(),
    val articleDetail: ArticleDetail = ArticleDetail()
): UiState