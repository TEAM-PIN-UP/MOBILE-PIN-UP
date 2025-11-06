package com.pinup.placePinup.ui.reviewwrite.successWriteReview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.PinlogDetail
import com.pinup.placePinup.domain.usecase.GetPinlogDetailUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
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