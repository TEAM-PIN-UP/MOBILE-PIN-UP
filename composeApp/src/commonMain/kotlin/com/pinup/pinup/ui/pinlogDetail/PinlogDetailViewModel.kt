package com.pinup.pinup.ui.pinlogDetail

import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState


class PinlogDetailViewModel(
) : BaseViewModel<PinlogUiState, UiEvent>(PinlogUiState()) {

    fun updateMyComment(query: String) {
        updateState {
            copy(
                myComment = query
            )
        }
    }
}

data class PinlogUiState(
    val commentList: List<Comment> = emptyList(),
    val myComment: String = "",
) : UiState