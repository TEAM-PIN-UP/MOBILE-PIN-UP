package com.pinup.pinup.ui.article.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.GetReviewedPlacesRequest
import com.pinup.pinup.domain.model.ArticleDetail
import com.pinup.pinup.domain.model.ArticlePlace
import com.pinup.pinup.domain.model.EditorPintsDetail
import com.pinup.pinup.domain.model.PagingArticle
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.usecase.AddBookmarkUseCase
import com.pinup.pinup.domain.usecase.DeleteBookmarkUseCase
import com.pinup.pinup.domain.usecase.GetEditorPintsDetailUseCase
import com.pinup.pinup.domain.usecase.GetEditorPintsUseCase
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class ArticleDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getEditorPintsDetailUseCase: GetEditorPintsDetailUseCase,
    private val getEditorPintsUseCase: GetEditorPintsUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase
) : BaseViewModel<ArticleDetailUiState, UiEvent>(ArticleDetailUiState()) {

    companion object {
        private const val PINTS_ID = "pintsId"

    }

    val pintsId = savedStateHandle.get<Int>(PINTS_ID) ?: 0

    init {
        getArticleDetail()
        getArticleList()
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

    fun updateBookmark(kakaoPlaceId: String, nowState: Boolean) = viewModelScope.launch {
        val result = if (nowState) {
            deleteBookmarkUseCase(kakaoPlaceId)
        } else {
            addBookmarkUseCase(kakaoPlaceId)
        }

        resultResponse(
            response = result,
            successCallback = {
                getArticleDetail()
            }
        )
    }

    fun getArticleList() = viewModelScope.launch {
        val request = GetReviewedPlacesRequest(
            swLatitude = "0.0",
            swLongitude = "0.0",
            neLatitude = "0.0",
            neLongitude = "0.0",
        )
        resultResponse(
            response = getEditorPintsUseCase(request),
            successCallback = { it ->
                updateState {
                    copy(
                        pintsListItem = it.filter { item -> item.id != pintsId },
                    )
                }
            }
        )
    }
}

data class ArticleDetailUiState(
    val editorPintsDetail: EditorPintsDetail = EditorPintsDetail(),
    val pintsListItem: List<PinchListItem> = emptyList(),
): UiState