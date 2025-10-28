package com.pinup.pinup.ui.my.pinch.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.usecase.DeletePintsUseCase
import com.pinup.pinup.domain.usecase.GetPintsDetailUseCase
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.util.currentDate
import com.pinup.pinup.util.toShortDateXd
import kotlinx.coroutines.launch

class PinchDetailViewModel (
    savedStateHandle: SavedStateHandle,
    private val getPintsDetailUseCase: GetPintsDetailUseCase,
    private val deletePintsUseCase: DeletePintsUseCase,
) : BaseViewModel<PinchDetailUiState, PinchDetailUiEvent>(PinchDetailUiState()) {

    companion object Companion {
        private const val PINTS_ID = "pintsId"
    }

    val pintsId = savedStateHandle.get<Int>(PINTS_ID) ?: 0

    fun getPintsDetail() = viewModelScope.launch {
        resultResponse(
            response = getPintsDetailUseCase(pintsId),
            successCallback = { result ->
                updateState {
                    copy(
                        title = result.title,
                        description = result.content,
                        createdAt = toShortDateXd(result.createdAt),
                        pinchList = result.placeSummaries.map {
                            Place(
                                kakaoPlaceId = it.kakaoPlaceId,
                                name = it.name,
                                address = it.address,
                                latitude = it.latitude,
                                longitude = it.longitude
                            )
                        }
                    )
                }
            }
        )
    }

    fun deletePinch() = viewModelScope.launch {
        resultResponse(
            response = deletePintsUseCase(pintsId),
            successCallback = {
                emitEvent(PinchDetailUiEvent.SuccessModify)
            }
        )
    }
}

data class PinchDetailUiState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = toShortDateXd(currentDate.toString()),
    val pinchList: List<Place> = emptyList(),
    val currentPosition: Position? = null,
    val cameraPosition: Position? = null,
) : UiState

sealed interface PinchDetailUiEvent : UiEvent {
    data object SuccessModify : PinchDetailUiEvent
}