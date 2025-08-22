package com.pinup.pinup.ui.reviewwrite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.PlaceRequest
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.usecase.EditPinlogUseCase
import com.pinup.pinup.domain.usecase.PostSeveralImagesUploadUseCase
import com.pinup.pinup.domain.usecase.RegisterReviewUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class WriteReviewViewModel (
    savedStateHandle: SavedStateHandle,
    private val registerReviewUseCase: RegisterReviewUseCase,
    private val imagesUploadUseCase: PostSeveralImagesUploadUseCase,
    private val editPinlogUseCase: EditPinlogUseCase,
) : BaseViewModel<WriteReviewUiState, WriteReviewUiEvent>(WriteReviewUiState()) {

    companion object {
        private const val REVIEW_ID = "reviewId"

    }

    val reviewId = savedStateHandle.get<Int>(REVIEW_ID) ?: 0

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

    fun onClickedImage(imgPath: ByteArray) = viewModelScope.launch {
        updateState {
            copy(
                clickedImage = imgPath
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
        resultResponse(
            response = imagesUploadUseCase(
                type = ImageUploadType.REVIEWS,
                files = files
            ),
            successCallback = ::uploadPinLog
        )
    }

    private fun uploadPinLog(images: List<String>) = viewModelScope.launch {
        val place = uiState.value.selectedPlace ?: return@launch
        val request = AddReviewRequest(
            reviewRequest = ReviewRequest(
                content = uiState.value.content,
                starRating = uiState.value.starRating,
                visitedDate = uiState.value.visitedDate,
                reviewImageUrls = images
            ),
            placeRequest = PlaceRequest(
                kakaoPlaceId = place.kakaoPlaceId,
                name = place.name,
                category = place.placeCategory,
                address = place.address,
                roadAddress = place.roadAddress,
                latitude = place.latitude,
                longitude = place.longitude
            )
        )

        resultResponse(
            response = registerReviewUseCase(request),
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
    val imagePaths: List<ByteArray> = emptyList(),
    val clickedImage: ByteArray = ByteArray(0)
) : UiState {
    val isEnableRegister: Boolean = (starRating != 0.0) && (content.length >= 10)
}

sealed interface WriteReviewUiEvent : UiEvent {
    data object MoveSelectDate : WriteReviewUiEvent
    data object MoveWriteReview : WriteReviewUiEvent
    data object SuccessWriteReview : WriteReviewUiEvent
}