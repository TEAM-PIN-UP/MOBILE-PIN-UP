package com.pinup.pinup.ui.pinlogDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.domain.usecase.DeleteCommentUseCase
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.EditCommentUseCase
import com.pinup.pinup.domain.usecase.PostCommentUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class PinlogDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val uploadCommentUseCase: PostCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val editCommentUseCase: EditCommentUseCase
) : BaseViewModel<PinlogUiState, PinlogUiEvent>(PinlogUiState()) {

    companion object {
        private const val REVIEW_ID = "reviewId"

    }

    val reviewId = savedStateHandle.get<Int>(REVIEW_ID) ?: 0
    private var commentId = -1

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

    fun updateEditMode(id: Int, comment: String){
        commentId = id
        updateMyComment(comment)
        updateState {
            copy(
                isEditComment = true
            )
        }
    }

    fun updateNormalMode(){
        updateState {
            copy(
                isEditComment = false,
                clickedReplyId = null,
                myComment = ""
            )
        }
    }

    fun uploadComment() = viewModelScope.launch {
        if (uiState.value.isEditComment) {
            editComment()
        } else {
            val request = CommentRequest(
                content = uiState.value.myComment,
                parentId = uiState.value.clickedReplyId
            )
            resultResponse(
                response = uploadCommentUseCase(reviewId, request),
                successCallback = {
                    //TODO 페이지 리로드
                }
            )
        }
    }

    private fun editComment() = viewModelScope.launch {
        resultResponse(
            response = editCommentUseCase(reviewId, commentId, uiState.value.myComment),
            successCallback = {
                //TODO 페이지 리로드
            }
        )
    }

    fun deleteComment(id: Int) = viewModelScope.launch {
        resultResponse(
            response = deleteCommentUseCase(reviewId, id),
            successCallback = {
                //TODO 페이지 리로드
            }
        )
    }

    fun updateClickedReplyId(id: Int) {
        updateState {
            copy(
                clickedReplyId = id
            )
        }
    }
}


data class PinlogUiState(
    val commentList: List<Comment> = emptyList(),
    val myComment: String = "",
    val clickedReplyId: Int? = null,
    val isEditComment: Boolean = false,
) : UiState

sealed interface PinlogUiEvent : UiEvent {
    data object SuccessDelete : PinlogUiEvent
}
