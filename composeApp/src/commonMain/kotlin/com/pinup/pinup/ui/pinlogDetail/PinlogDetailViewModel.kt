package com.pinup.pinup.ui.pinlogDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class PinlogDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val deletePinlogUseCase: DeletePinlogUseCase,
) : BaseViewModel<PinlogUiState, PinlogUiEvent>(PinlogUiState()) {

    val reviewId = savedStateHandle.get<Int>(REVIEW_ID) ?: 0

    fun updateMyComment(query: String) {
        updateState {
            copy(
                myComment = query
            )
        }
    }

    fun deleteReview() = viewModelScope.launch {
        resultResponse(
            response = deletePinlogUseCase(reviewId),
            successCallback = {
                emitEvent(PinlogUiEvent.SuccessDelete)
            }
        )
    }

    companion object {
        private const val REVIEW_ID = "reviewId"

    }
}


data class PinlogUiState(
    val commentList: List<Comment> = emptyList(),
    val myComment: String = "",
) : UiState

sealed interface PinlogUiEvent : UiEvent {
    data object SuccessDelete : PinlogUiEvent
}
