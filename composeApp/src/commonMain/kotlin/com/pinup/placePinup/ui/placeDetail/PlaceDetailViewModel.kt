package com.pinup.placePinup.ui.placeDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.DetailPlace
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.usecase.AddBookmarkUseCase
import com.pinup.placePinup.domain.usecase.DeleteBookmarkUseCase
import com.pinup.placePinup.domain.usecase.DeletePinlogUseCase
import com.pinup.placePinup.domain.usecase.GetDetailPlaceUseCase
import com.pinup.placePinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.map.MapUiEvent
import com.pinup.placePinup.ui.model.ChipState
import kotlinx.coroutines.launch


class PlaceDetailViewModel (
    savedStateHandle: SavedStateHandle,
    private val getDetailPlaceUseCase: GetDetailPlaceUseCase,
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
) : BaseViewModel<PlaceDetailViewUiState, UiEvent>(PlaceDetailViewUiState()) {

    companion object {
        private const val PLACE_ID = "kakaoPlaceId"

    }

    val kakaoPlaceId = savedStateHandle.get<String>(PLACE_ID) ?: ""

    init {
        getDetailPlace(kakaoPlaceId)
    }

    private fun getDetailPlace(kakaoPlaceId: String) = viewModelScope.launch {
        resultResponse(
            response = getDetailPlaceUseCase(
                kakaoPlaceId = kakaoPlaceId,
                currentLatitude = null,
                currentLongitude = null
            ),
            successCallback = {
                updateState {
                    copy(
                        detailPlace = it
                    )
                }
            }
        )
    }

    fun deleteReview(id: Int) = viewModelScope.launch {
        resultResponse(
            response = deletePinlogUseCase(id),
            successCallback = {
                uiState.value.detailPlace.mapPlace.let {
                    getDetailPlace(it.kakaoPlaceId)
                }
                emitEvent(MapUiEvent.SuccessDelete)
            }
        )
    }

    fun likeChanged(id: Int, isLike: Boolean) = viewModelScope.launch {
        resultResponse(
            response = postReviewLikeChangeUseCase(id, isLike),
            successCallback = {
                uiState.value.detailPlace.mapPlace.let {
                    getDetailPlace(it.kakaoPlaceId)
                }
            }
        )
    }

    fun updateBookmark(kakaoPlaceId: String, nowState: Boolean) = viewModelScope.launch {
        val result = if (nowState) {
            deleteBookmarkUseCase(kakaoPlaceId)
        } else {
            addBookmarkUseCase(kakaoPlaceId)
        }

        resultResponse(
            response = result,
            successCallback = {
                updateState {
                    copy(
                        detailPlace = detailPlace.copy(
                            mapPlace = detailPlace.mapPlace.copy(
                                bookmark = detailPlace.mapPlace.bookmark.not()
                            )
                        )
                    )
                }
            }
        )
    }
}

data class PlaceDetailViewUiState(
    val detailPlace: DetailPlace = DetailPlace(),
    val chipStates: List<ChipState> = ChipState.default,
    val sortType: SortType = SortType.LATEST,
    val profileUrl: String = "",
) : UiState