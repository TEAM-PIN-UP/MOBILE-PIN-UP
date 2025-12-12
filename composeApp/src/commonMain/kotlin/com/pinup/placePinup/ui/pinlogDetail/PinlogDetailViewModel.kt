package com.pinup.placePinup.ui.pinlogDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.report.UserBlockRequest
import com.pinup.placePinup.data.request.review.CommentRequest
import com.pinup.placePinup.domain.model.Comment
import com.pinup.placePinup.domain.model.PinlogDetail
import com.pinup.placePinup.domain.model.UserInfo
import com.pinup.placePinup.domain.usecase.DeleteCommentUseCase
import com.pinup.placePinup.domain.usecase.DeletePinlogUseCase
import com.pinup.placePinup.domain.usecase.EditCommentUseCase
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.domain.usecase.GetPinlogDetailUseCase
import com.pinup.placePinup.domain.usecase.PostCommentUseCase
import com.pinup.placePinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.placePinup.domain.usecase.PostUserBlockUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class PinlogDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val uploadCommentUseCase: PostCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val editCommentUseCase: EditCommentUseCase,
    private val getPinlogDetailUseCase: GetPinlogDetailUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val postUserBlockUseCase: PostUserBlockUseCase,
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

    fun likeChanged(isLike: Boolean) = viewModelScope.launch {
        resultResponse(
            response = postReviewLikeChangeUseCase(reviewId, isLike),
            successCallback = {
                getPinlogDetail()
            }
        )
    }

    private fun getPinlogDetail() = viewModelScope.launch{
        resultResponse(
            response = getPinlogDetailUseCase(reviewId),
            successCallback = {
                updateState {
                    copy(
                        pinlogDetail = it,
                        isRefreshing = false
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
                    updateClickedReplyId(null)
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

    fun updateClickedReplyId(id: Int?) {
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

    fun refreshView() = viewModelScope.launch{
        updateState {
            copy(
                isRefreshing = true
            )
        }
        delay(1.seconds)
        getPinlogDetail()
    }

    fun onProfileClick(name: String) = viewModelScope.launch {
        getMyProfileUseCase().collectLatest {
            if(it.nickname != name) emitEvent(PinlogUiEvent.OnMoveUserProfile(name))
        }
    }

    fun blockUser(id: Int) = viewModelScope.launch {
        val request = UserBlockRequest(id)
        resultResponse(
            response = postUserBlockUseCase(request),
            successCallback = { emitEvent(PinlogUiEvent.SuccessBlockUser) }
        )
    }
}


data class PinlogUiState(
    val pinlogDetail: PinlogDetail = PinlogDetail(),
    val commentList: List<Comment> = emptyList(),
    val myComment: String = "",
    val clickedReplyId: Int? = null,
    val isEditComment: Boolean = false,
    val userInfo: UserInfo = UserInfo(),
    val isRefreshing: Boolean = false
) : UiState

sealed interface PinlogUiEvent : UiEvent {
    data object SuccessDelete : PinlogUiEvent
    data object SuccessBlockUser : PinlogUiEvent
    data class OnMoveUserProfile(val name: String): PinlogUiEvent
}
