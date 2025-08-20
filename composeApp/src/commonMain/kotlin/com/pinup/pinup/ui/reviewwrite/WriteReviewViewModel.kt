package com.pinup.pinup.ui.reviewwrite

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.WriteReview
import com.pinup.pinup.domain.usecase.RegisterReviewUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class WriteReviewViewModel (
    private val registerReviewUseCase: RegisterReviewUseCase
) : BaseViewModel<WriteReviewUiState, WriteReviewUiEvent>(WriteReviewUiState()) {

    var kakaoPlaceId: String? = null

    fun updateContent(inputText: String) {
        updateState {
            copy(
                content = inputText
            )
        }
    }


    fun selectPlace(place: Place) = viewModelScope.launch {
        updateState {
            copy(
                selectedPlace = place
            )
        }
        emitEvent(WriteReviewUiEvent.MoveSelectDate)
    }

    fun selectDate(visitedDate: String) = viewModelScope.launch {
        updateState {
            copy(
                visitedDate = visitedDate
            )
        }
    }

    fun addImage(imgPath: ByteArray) = viewModelScope.launch {
        updateState {
            copy(
                imagePaths = imagePaths + imgPath
            )
        }
    }

    fun removeImage(imgPath: ByteArray) = viewModelScope.launch {
        updateState {
            copy(
                imagePaths = imagePaths - imgPath
            )
        }
    }

    fun updateRating(rating: Double) = viewModelScope.launch {
        updateState{
            copy(
                starRating = rating
            )
        }
    }

    fun registerReview() = viewModelScope.launch {
        val files = uiState.value.imagePaths
        val writeReview = WriteReview(
            content = uiState.value.content,
            starRating = uiState.value.starRating.toDouble(),
            visitedDate = uiState.value.visitedDate
        )
        val place = uiState.value.selectedPlace ?: return@launch
        resultResponse(
            response = registerReviewUseCase(files = files, writeReview = writeReview, place = place),
            successCallback = {
                kakaoPlaceId = it
                emitEvent(WriteReviewUiEvent.SuccessWriteReview)
            }
        )
    }
}

data class WriteReviewUiState(
    val selectedPlace: Place? = null,
    val visitedDate: String = "",
    val starRating: Double = 0.0,
    val content: String = "",
    val imagePaths: List<ByteArray> = emptyList()
) : UiState {
    val isEnableRegister: Boolean = (starRating != 0.0) && (content.length >= 10)
}

sealed interface WriteReviewUiEvent : UiEvent {
    data object MoveSelectDate : WriteReviewUiEvent
    data object MoveWriteReview : WriteReviewUiEvent
    data object SuccessWriteReview : WriteReviewUiEvent
}