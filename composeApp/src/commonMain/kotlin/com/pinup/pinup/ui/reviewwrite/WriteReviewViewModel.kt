package com.pinup.pinup.ui.reviewwrite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.PlaceRequest
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.usecase.EditPinlogUseCase
import com.pinup.pinup.domain.usecase.GetDetailPlaceUseCase
import com.pinup.pinup.domain.usecase.GetPinlogDetailUseCase
import com.pinup.pinup.domain.usecase.PostImageUploadUseCase
import com.pinup.pinup.domain.usecase.RegisterReviewUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch
import kotlin.Double
import kotlin.String

class WriteReviewViewModel (
    savedStateHandle: SavedStateHandle,
    private val registerReviewUseCase: RegisterReviewUseCase,
    private val imageUploadUseCase: PostImageUploadUseCase,
    private val editPinlogUseCase: EditPinlogUseCase,
    private val getPinlogDetailUseCase: GetPinlogDetailUseCase,
    private val getDetailPlaceUseCase: GetDetailPlaceUseCase,
) : BaseViewModel<WriteReviewUiState, WriteReviewUiEvent>(WriteReviewUiState()) {

    companion object {
        private const val REVIEW_ID = "reviewId"
        private const val PLACE_ID = "placeId"

    }

    val reviewId = savedStateHandle.get<Int>(REVIEW_ID) ?: 0
    val placeId = savedStateHandle.get<String>(PLACE_ID)

    var writeReviewId: Int? = null

    init {
        if (reviewId != 0) {
            getPinlogDetail()
        }

        if (placeId != null) {
            getSelectPlaceDetail(placeId)
        }
    }

    private fun getSelectPlaceDetail(id: String) = viewModelScope.launch {
        resultResponse(
            response = getDetailPlaceUseCase(id, null, null),
            successCallback = {
                selectPlace(
                    Place(
                        name = it.mapPlace.name,
                        address = it.mapPlace.roadAddress,
                        averageStarRating = it.mapPlace.averageStarRating,
                        categoryCode = "",
                        description = "",
                        kakaoPlaceId = id,
                        latitude = it.mapPlace.longitude,
                        longitude = it.mapPlace.latitude,
                        placeCategory = it.mapPlace.placeCategory.name,
                        reviewCount = it.mapPlace.reviewCount,
                        roadAddress = it.mapPlace.roadAddress
                    )
                )
            }
        )
    }

    private fun getDetailPlace(id: String) = viewModelScope.launch {
        resultResponse(
            response = getDetailPlaceUseCase(id, null, null),
            successCallback = {
                updatePlace(
                    Place(
                        name = it.mapPlace.name,
                        address = it.mapPlace.roadAddress,
                        averageStarRating = it.mapPlace.averageStarRating,
                        categoryCode = "",
                        description = "",
                        kakaoPlaceId = id,
                        latitude = it.mapPlace.longitude,
                        longitude = it.mapPlace.latitude,
                        placeCategory = it.mapPlace.placeCategory.name,
                        reviewCount = it.mapPlace.reviewCount,
                        roadAddress = it.mapPlace.roadAddress
                    )
                )
            }
        )
    }

    private fun getPinlogDetail() = viewModelScope.launch {
        resultResponse(
            response = getPinlogDetailUseCase(reviewId),
            successCallback = {
                getDetailPlace(it.kakaoPlaceId)
                it.reviewImageUrls.forEach {
                    addImage(it)
                }
                updateRating(it.starRating)
                updateContent(it.content)
            }
        )
    }

    fun updateContent(inputText: String) {
        updateState {
            copy(
                content = inputText
            )
        }
    }

    fun updatePlace(place: Place) {
        updateState {
            copy(
                selectedPlace = place
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

    fun uploadImage(imgPath: ByteArray) = viewModelScope.launch {
        resultResponse(
            response = imageUploadUseCase(
                type = ImageUploadType.REVIEWS,
                image = imgPath
            ),
            successCallback = ::addImage
        )
    }

    private fun addImage(imgPath: String) = viewModelScope.launch {
        updateState {
            copy(
                imagePaths = imagePaths + imgPath
            )
        }
    }

    fun onClickedImage(imgPath: String) = viewModelScope.launch {
        updateState {
            copy(
                clickedImage = imgPath
            )
        }
    }


    fun removeImage(imgPath: String) = viewModelScope.launch {
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

    fun uploadPinLog() = viewModelScope.launch {
        if (reviewId != 0) {
            editPinlog()
            return@launch
        }
        val place = uiState.value.selectedPlace ?: return@launch
        val request = AddReviewRequest(
            reviewRequest = ReviewRequest(
                content = uiState.value.content,
                starRating = uiState.value.starRating,
                visitedDate = uiState.value.visitedDate,
                reviewImageUrls = uiState.value.imagePaths
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
                writeReviewId = it
                emitEvent(WriteReviewUiEvent.SuccessWriteReview)
            }
        )
    }

    private fun editPinlog() = viewModelScope.launch {
        val request = ReviewRequest(
            content = uiState.value.content,
            starRating = uiState.value.starRating,
            visitedDate = null,
            reviewImageUrls = uiState.value.imagePaths
        )

        resultResponse(
            response = editPinlogUseCase(reviewId, request),
            successCallback = {
                emitEvent(WriteReviewUiEvent.SuccessEditReview)
            }
        )
    }
}

data class WriteReviewUiState(
    val selectedPlace: Place? = null,
    val visitedDate: String = "",
    val starRating: Double = 0.0,
    val content: String = "",
    val imagePaths: List<String> = emptyList(),
    val clickedImage: String = ""
) : UiState {
    val isEnableRegister: Boolean = (starRating != 0.0) && (content.length >= 10)
}

sealed interface WriteReviewUiEvent : UiEvent {
    data object MoveSelectDate : WriteReviewUiEvent
    data object MoveWriteReview : WriteReviewUiEvent
    data object SuccessWriteReview : WriteReviewUiEvent
    data object SuccessEditReview : WriteReviewUiEvent
}