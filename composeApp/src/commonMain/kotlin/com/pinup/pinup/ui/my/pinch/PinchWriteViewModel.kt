package com.pinup.pinup.ui.my.pinch

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.pints.ModifyPintsRequest
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.usecase.EditPintsUseCase
import com.pinup.pinup.domain.usecase.GetPintsDetailUseCase
import com.pinup.pinup.domain.usecase.RegisterPintsUseCase
import com.pinup.pinup.domain.usecase.SearchPlacesUseCase
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

class PinchWriteViewModel (
    savedStateHandle: SavedStateHandle,
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val getPintsDetailUseCase: GetPintsDetailUseCase,
    private val registerPintsUseCase: RegisterPintsUseCase,
    private val editPintsUseCase: EditPintsUseCase,
    val locationTracker: LocationTracker
) : BaseViewModel<PinchWriteUiState, PinchWriteUiEvent>(PinchWriteUiState()) {

    companion object {
        private const val PINTS_ID = "pintsId"
    }

    val pintsId = savedStateHandle.get<Int>(PINTS_ID) ?: 0

    init {
        if (pintsId != 0) {
            getPintsDetail()
        }

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

    fun updateTitle(title: String) {
        updateState {
            copy(
                title = title
            )
        }
    }

    fun updateDescription(des: String) {
        updateState {
            copy(
                description = des
            )
        }
    }

    fun updatePinchName(index: Int, name: String) {
        getSearchedPlaceList(name)
        updateState {
            copy(
                pinchList = pinchList.mapIndexed { i, place ->
                    if (i == index) {
                        place.copy(name = name)
                    } else {
                        place
                    }
                }
            )
        }
    }

    private fun getSearchedPlaceList(search: String) = viewModelScope.launch {
        resultResponse(
            response = searchPlacesUseCase(search),
            successCallback = { result ->
                updateState {
                    copy(
                        searchedList = result.take(4)
                    )
                }
            }
        )
    }

    fun deletePinch(index: Int) {
        updateState {
            if (index !in pinchList.indices) return@updateState this

            val reordered =
                pinchList
                    .filterIndexed { i, _ -> i != index }
                    .plus(Place())
            val lastIdx = reordered.indexOfLast { it.kakaoPlaceId.isNotEmpty() }
            updatePosition(Position(reordered[lastIdx].latitude,reordered[lastIdx].longitude))
            copy(pinchList = reordered)
        }
    }

    fun registerPints() = viewModelScope.launch {
        val request = ModifyPintsRequest(
            title = uiState.value.title,
            content = uiState.value.description,
            kakaoPlaceIds = uiState.value.pinchList.filter { it.kakaoPlaceId != "" }.map { it.kakaoPlaceId },
        )

        resultResponse(
            response = if (pintsId != 0) editPintsUseCase(pintsId, request) else registerPintsUseCase(request),
            successCallback = {
                emitEvent(PinchWriteUiEvent.SuccessModify)
            }
        )
    }

    fun moveItem(from: Int, to: Int) {
        if (from == to) return
        updateState {
            val new = pinchList.toMutableList().apply {
                add(to, removeAt(from))
            }
            copy(pinchList = new)
        }
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

data class PinchWriteUiState(
    val title: String = "",
    val description: String = "",
    val createdAt: String = toShortDateXd(currentDate.toString()),
    val pinchList: List<Place> = List(5) { Place() },
    val searchedList: List<Place> = emptyList(),
    val currentPosition: Position? = null,
    val cameraPosition: Position? = null,
) : UiState

sealed interface PinchWriteUiEvent : UiEvent {
    data object SuccessModify : PinchWriteUiEvent
}