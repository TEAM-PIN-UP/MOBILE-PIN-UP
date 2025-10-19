package com.pinup.pinup.ui.my.pinch.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.usecase.DeletePintsUseCase
import com.pinup.pinup.domain.usecase.GetMemberInfoUseCase
import com.pinup.pinup.domain.usecase.GetPintsDetailUseCase
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.util.currentDate
import com.pinup.pinup.util.toShortDateXd
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class PinchDetailViewModel (
    savedStateHandle: SavedStateHandle,
    private val getPintsDetailUseCase: GetPintsDetailUseCase,
    private val deletePintsUseCase: DeletePintsUseCase,
    val locationTracker: LocationTracker
) : BaseViewModel<PinchDetailUiState, PinchDetailUiEvent>(PinchDetailUiState()) {

    companion object Companion {
        private const val PINTS_ID = "pintsId"
    }

    val pintsId = savedStateHandle.get<Int>(PINTS_ID) ?: 0

    init {
        getPintsDetail()
        initCollectLocation()
    }

    private fun initCollectLocation() = viewModelScope.launch {
        locationTracker.startTracking()
        locationTracker.getLocationsFlow()
            .distinctUntilChanged()
            .collectLatest {
                val myLocation = Position(it.latitude, it.longitude)
                hLog(myLocation.toString())
                updateCameraPosition(myLocation)
                updatePosition(myLocation)
            }
    }

    private fun getPintsDetail() = viewModelScope.launch {
        resultResponse(
            response = getPintsDetailUseCase(pintsId),
            successCallback = { result ->
                updateState {
                    copy(
                        title = result.title,
                        description = result.content,
                        createdAt = toShortDateXd(result.createdAt),
                    )
                }
                result.placeSummaries.forEachIndexed { index, item ->
                    val place = Place(
                        kakaoPlaceId = item.kakaoPlaceId,
                        name = item.name,
                        address = item.address,
                        latitude = item.latitude,
                        longitude = item.latitude
                    )
                    updatePlace(place = place, index = index)
                }
            }
        )
    }

    private fun updateCameraPosition(position: Position?) {
        updateState {
            copy(
                cameraPosition = position
            )
        }
    }

    private fun updatePosition(position: Position) {
        updateState {
            copy(
                currentPosition = position
            )
        }
    }

    fun deletePinch() = viewModelScope.launch {
        resultResponse(
            response = deletePintsUseCase(pintsId),
            successCallback = {
                emitEvent(PinchDetailUiEvent.SuccessModify)
            }
        )
    }

    fun updatePlace(place: Place, index: Int) {
        updateState {
            copy(
                cameraPosition = Position(
                    latitude = place.latitude,
                    longitude = place.longitude
                ),
                pinchList = pinchList.mapIndexed { i, p ->
                    if (i == index) {
                        place
                    } else {
                        p
                    }
                }
            )
        }
    }
}

data class PinchDetailUiState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = toShortDateXd(currentDate.toString()),
    val pinchList: List<Place> = List(5) { Place() },
    val currentPosition: Position? = null,
    val cameraPosition: Position? = null,
) : UiState

sealed interface PinchDetailUiEvent : UiEvent {
    data object SuccessModify : PinchDetailUiEvent
}