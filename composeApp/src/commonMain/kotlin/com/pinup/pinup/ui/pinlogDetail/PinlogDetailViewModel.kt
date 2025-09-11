package com.pinup.pinup.ui.pinlogDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.domain.model.PinlogDetail
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.usecase.DeleteCommentUseCase
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.EditCommentUseCase
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
import com.pinup.pinup.domain.usecase.GetPinlogDetailUseCase
import com.pinup.pinup.domain.usecase.PostCommentUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PinlogDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val uploadCommentUseCase: PostCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val editCommentUseCase: EditCommentUseCase,
    private val getPinlogDetailUseCase: GetPinlogDetailUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
) : BaseViewModel<PinlogUiState, PinlogUiEvent>(PinlogUiState()) {

    companion object {
        private const val REVIEW_ID = "reviewId"

    }

    val reviewId = savedStateHandle.get<Int>(REVIEW_ID) ?: 0

    init {
        getPinlogDetail()
        getUserInfo()
    }
    private var commentId = -1

    fun updateMyComment(query: String) {
        updateState {
            copy(
                myComment = query
            )
        }
    }

    private fun getPinlogDetail() = viewModelScope.launch{
        resultResponse(
            response = getPinlogDetailUseCase(reviewId),
            successCallback = {
                updateState {
                    copy(
                        pinlogDetail = it,
                    )
                }
            }
        )
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
                    updateMyComment("")
                    getPinlogDetail()
                }
            )
        }
    }

    private fun editComment() = viewModelScope.launch {
        resultResponse(
            response = editCommentUseCase(reviewId, commentId, uiState.value.myComment),
            successCallback = {
                getPinlogDetail()
            }
        )
    }

    fun deleteComment(id: Int) = viewModelScope.launch {
        resultResponse(
            response = deleteCommentUseCase(reviewId, id),
            successCallback = {
                getPinlogDetail()
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

    private fun getUserInfo() = viewModelScope.launch {
        getMyProfileUseCase()
            .collectLatest { profile ->
                updateState {
                    copy(
                        userInfo = profile
                    )
                }
            }
    }
}


data class PinlogUiState(
    val pinlogDetail: PinlogDetail = PinlogDetail(),
    val commentList: List<Comment> = emptyList(),
    val myComment: String = "",
    val clickedReplyId: Int? = null,
    val isEditComment: Boolean = false,
    val userInfo: UserInfo = UserInfo()
) : UiState

sealed interface PinlogUiEvent : UiEvent {
    data object SuccessDelete : PinlogUiEvent
}
