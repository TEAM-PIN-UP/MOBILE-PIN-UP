package com.pinup.pinup.ui.pinlogDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class PinlogDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PinlogUiState, UiEvent>(PinlogUiState()) {

    companion object {
        private const val REVIEW_ID = "reviewId"
        private const val PAGE_SIZE = 20

    }

    val reviewId = savedStateHandle.get<Int>(REVIEW_ID) ?: 0

    fun updateMyComment(query: String) {
        updateState {
            copy(
                myComment = query
            )
        }
    }

    fun deleteReview() = viewModelScope.launch {

    }
}


data class PinlogUiState(
    val commentList: List<Comment> = emptyList(),
    val myComment: String = "",
) : UiState