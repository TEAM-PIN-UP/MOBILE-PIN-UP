package com.pinup.pinup.ui.reviewwrite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.WriteReview
import com.pinup.pinup.domain.usecase.RegisterReviewUseCase
import com.pinup.pinup.platform.hLog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class WriteReviewViewModel (
    private val registerReviewUseCase: RegisterReviewUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WriteReviewUiState())
    val uiState: StateFlow<WriteReviewUiState>
        get() = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<WriteReviewUiEvent>()
    val uiEvent: SharedFlow<WriteReviewUiEvent>
        get() = _uiEvent.asSharedFlow()
    var kakaoPlaceId: String? = null

    fun updateContent(inputText: String) {
        _uiState.update {
            it.copy(
                content = inputText
            )
        }
    }


    fun selectPlace(place: Place) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                selectedPlace = place
            )
        }
        _uiEvent.emit(WriteReviewUiEvent.MoveSelectDate)
    }

    fun selectDate(visitedDate: String) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                visitedDate = visitedDate
            )
        }
        _uiEvent.emit(WriteReviewUiEvent.MoveWriteReview)
    }

    fun addImage(imgPath: ByteArray) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                imagePaths = it.imagePaths + imgPath
            )
        }
    }

    fun removeImage(imgPath: ByteArray) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                imagePaths = it.imagePaths - imgPath
            )
        }
    }

    fun updateRating(rating: Int) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                starRating = rating
            )
        }
    }

    fun registerReview() = viewModelScope.launch {
        val files = _uiState.value.imagePaths
        val writeReview = WriteReview(
            content = _uiState.value.content,
            starRating = _uiState.value.starRating.toDouble(),
            visitedDate = _uiState.value.visitedDate
        )
        val place = _uiState.value.selectedPlace ?: return@launch
        when (val result = registerReviewUseCase(files = files, writeReview = writeReview, place = place)) {
            is PResult.Fail -> {
                hLog("fail >>> ${result.failState}")
                hLog("writeReview >>> ${writeReview}")
                hLog("fail >>> ${place}")

            }

            is PResult.Success -> {
                kakaoPlaceId = result.data
                _uiEvent.emit(WriteReviewUiEvent.SuccessWriteReview)
            }
        }
    }
}

data class WriteReviewUiState(
    val selectedPlace: Place? = null,
    val visitedDate: String = "",
    val starRating: Int = 0,
    val content: String = "",
    val imagePaths: List<ByteArray> = emptyList()
)

sealed interface WriteReviewUiEvent {
    data object MoveSelectDate : WriteReviewUiEvent
    data object MoveWriteReview : WriteReviewUiEvent
    data object SuccessWriteReview : WriteReviewUiEvent
}