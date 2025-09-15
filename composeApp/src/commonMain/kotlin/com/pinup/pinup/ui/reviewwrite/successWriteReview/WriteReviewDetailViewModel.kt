package com.pinup.pinup.ui.reviewwrite.successWriteReview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PinlogDetail
import com.pinup.pinup.domain.usecase.GetPinlogDetailUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class WriteReviewDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getPinlogDetailUseCase: GetPinlogDetailUseCase,
) : BaseViewModel<WriteReviewDetailUiState, UiEvent>(WriteReviewDetailUiState()) {

    companion object {
        private const val REVIEW_ID = "reviewId"

    }

    val reviewId = savedStateHandle.get<Int>(REVIEW_ID) ?: 0

    init {
        getPinlogDetail()
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
}


data class WriteReviewDetailUiState(
    val pinlogDetail: PinlogDetail = PinlogDetail(),
) : UiState