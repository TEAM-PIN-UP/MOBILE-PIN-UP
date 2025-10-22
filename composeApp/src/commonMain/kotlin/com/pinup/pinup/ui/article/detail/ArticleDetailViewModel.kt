package com.pinup.pinup.ui.article.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.ArticleDetail
import com.pinup.pinup.domain.model.ArticlePlace
import com.pinup.pinup.domain.model.EditorPintsDetail
import com.pinup.pinup.domain.model.PagingArticle
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.usecase.GetEditorPintsDetailUseCase
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getEditorPintsDetailUseCase: GetEditorPintsDetailUseCase
) : BaseViewModel<ArticleDetailUiState, UiEvent>(ArticleDetailUiState()) {

    companion object {
        private const val PINTS_ID = "pintsId"

    }

    val pintsId = savedStateHandle.get<Int>(PINTS_ID) ?: 0

    init {
        getArticleDetail()
    }

    fun getArticleDetail() = viewModelScope.launch {
        resultResponse(
            response = getEditorPintsDetailUseCase(pintsId),
            successCallback = {
                updateState {
                    copy(
                        editorPintsDetail = it
                    )
                }
            }
        )
    }
}

data class ArticleDetailUiState(
    val editorPintsDetail: EditorPintsDetail = EditorPintsDetail()
): UiState