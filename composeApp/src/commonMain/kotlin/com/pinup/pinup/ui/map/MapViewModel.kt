package com.pinup.pinup.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.LocationBound
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.usecase.AddBookmarkUseCase
import com.pinup.pinup.domain.usecase.DeleteBookmarkUseCase
import com.pinup.pinup.domain.usecase.GetDetailPlaceUseCase
import com.pinup.pinup.domain.usecase.GetReviewedPlacesUseCase
import com.pinup.pinup.event.DetailPlaceEventBus
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.model.ChipState
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MapViewModel (
    private val getReviewedPlacesUseCase: GetReviewedPlacesUseCase,
    private val getDetailPlaceUseCase: GetDetailPlaceUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    val locationTracker: LocationTracker
) : ViewModel() {
    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState: StateFlow<MapUiState>
        get() = _mapUiState.asStateFlow()
    private val _uiEvent = MutableSharedFlow<MapUiEvent>()
    val uiEvent: SharedFlow<MapUiEvent>
        get() = _uiEvent.asSharedFlow()

    init {
        initDetailPlaceEventBus()
        initCollectLocation()
    }

    private fun initCollectLocation() = viewModelScope.launch {
        hLog("위치 수집 시작")
        locationTracker.getLocationsFlow()
            .distinctUntilChanged()
            .collectLatest {
                val myLocation = Position(it.latitude, it.longitude)
                hLog("myLocation >>> ${myLocation}")
                if (_mapUiState.value.currentPosition == null || _mapUiState.value.isFocusLocation) {
                    updateCameraPosition(myLocation)
                }
                updatePosition(myLocation)
            }
    }

    @OptIn(FlowPreview::class)
    private fun initDetailPlaceEventBus() = viewModelScope.launch {
        DetailPlaceEventBus.detailPlaceEvent
            .debounce(300)
            .collectLatest { kakaoId ->
                getDetailPlace(kakaoId)
            }
    }

    private fun updatePosition(position: Position) = viewModelScope.launch {
        _mapUiState.update {
            it.copy(
                currentPosition = position
            )
        }
    }

    fun updateShowBookmarks() = viewModelScope.launch {
        hLog("updateShowBookmarks")
        _mapUiState.update {
            it.copy(
                isShowBookmarks = !it.isShowBookmarks
            )
        }
    }

    fun updateFocusLocation(value: Boolean) = viewModelScope.launch {
        _mapUiState.update {
            it.copy(
                isFocusLocation = value
            )
        }

        if (value) {
            updateCameraPosition(_mapUiState.value.currentPosition)
        }
    }

    private fun updateCameraPosition(position: Position?) = viewModelScope.launch {
        _mapUiState.update {
            it.copy(
                cameraPosition = position
            )
        }
    }


    fun getPlaces(cameraState: CameraState) = viewModelScope.launch {
        if (cameraState.isMoving.not()) {
            updateCameraPosition(cameraState.position)
            val latLngBounds = cameraState.contentBounds
            val request = _mapUiState.value.locationBound.copy(
                neLatitude = latLngBounds.northEast.latitude.toString(),
                neLongitude = latLngBounds.northEast.longitude.toString(),
                swLatitude = latLngBounds.southWest.latitude.toString(),
                swLongitude = latLngBounds.southWest.longitude.toString(),
            )
            _mapUiState.value.locationBound = request
            val filter = _mapUiState.value.searchUiState.chipStates.find { it.isSelected }?.type ?: Category.ALL
            val sortType = _mapUiState.value.searchUiState.sortType
            val currentLatLng = if (sortType == SortType.NEAR) _mapUiState.value.currentPosition else null
            when (val result = getReviewedPlacesUseCase(request, currentLatLng, sortType, filter)) {
                is PResult.Fail -> {
                    hLog("fail >> ${result.failState}")
                }

                is PResult.Success -> {
                    _mapUiState.update {
                        it.copy(
                            searchUiState = it.searchUiState.copy(
                                reviewedPlaces = result.data
                            ),
                        )
                    }
                }
            }
        } else {
            if (cameraState.reason == CameraState.Reason.GESTURE && _mapUiState.value.isFocusLocation) {
                updateFocusLocation(false)
            }
        }
    }

    fun updateChipState(chipState: ChipState) = viewModelScope.launch(Dispatchers.IO) {
        val request = _mapUiState.value.locationBound
        val sortType = _mapUiState.value.searchUiState.sortType
        val currentLatLng = if (sortType == SortType.NEAR) _mapUiState.value.currentPosition else null
        when (val result = getReviewedPlacesUseCase(request, currentLatLng, sortType, chipState.type)) {
            is PResult.Fail -> {
                hLog("fail >> ${result.failState}")
            }

            is PResult.Success -> {
                _mapUiState.update {
                    it.copy(
                        searchUiState = it.searchUiState.copy(
                            reviewedPlaces = result.data,
                            chipStates = _mapUiState.value.searchUiState.chipStates.map { chip ->
                                chip.copy(
                                    isSelected = chip == chipState
                                )
                            }
                        ),
                    )
                }
            }
        }
    }

    fun getDetailPlace(kakaoPlaceId: String) = viewModelScope.launch {
        if (_mapUiState.value.placeDetailUiState.detailPlace?.mapPlace?.kakaoPlaceId == kakaoPlaceId) return@launch
        when (val result = getDetailPlaceUseCase(
            kakaoPlaceId = kakaoPlaceId,
            currentLatitude = _mapUiState.value.currentPosition?.latitude?.toString(),
            currentLongitude = _mapUiState.value.currentPosition?.longitude?.toString()
        )) {
            is PResult.Fail -> {
                hLog("fail >> ${result.failState}")
            }

            is PResult.Success -> {
                _mapUiState.update {
                    it.copy(
                        placeDetailUiState = PlaceDetailUiState(result.data),
                        cameraPosition = Position(result.data.mapPlace.latitude, result.data.mapPlace.longitude),
                        isFocusLocation = false
                    )
                }
            }
        }
    }

    fun clearDetailPlace() = viewModelScope.launch {
        _mapUiState.update {
            it.copy(
                placeDetailUiState = PlaceDetailUiState()
            )
        }
    }

    fun updateBookmark(kakaoPlaceId: String, nowState: Boolean) = viewModelScope.launch {
        val result = if (nowState) {
            deleteBookmarkUseCase(kakaoPlaceId)
        } else {
            addBookmarkUseCase(kakaoPlaceId)
        }

        when (result) {
            is PResult.Fail -> {
                hLog("fail >> ${result.failState}")
            }
            is PResult.Success -> {
                hLog("success >")
                _mapUiState.update {
                    it.copy(
                        placeDetailUiState = it.placeDetailUiState.copy(
                            detailPlace = it.placeDetailUiState.detailPlace?.copy(
                                mapPlace = it.placeDetailUiState.detailPlace.mapPlace.copy(
                                   bookmark = it.placeDetailUiState.detailPlace.mapPlace.bookmark.not()
                                )
                            )
                        )
                    )
                }
            }
        }
    }

    fun updateSortType(sortType: SortType) = viewModelScope.launch {
        val request = _mapUiState.value.locationBound
        val chipState = _mapUiState.value.searchUiState.chipStates.first { it.isSelected }
        val currentLatLng = if (sortType == SortType.NEAR) _mapUiState.value.currentPosition else null
        when (val result = getReviewedPlacesUseCase(request, currentLatLng, sortType, chipState.type)) {
            is PResult.Fail -> {
                hLog("fail >> ${result.failState}")
            }

            is PResult.Success -> {
                _mapUiState.update {
                    it.copy(
                        searchUiState = it.searchUiState.copy(
                            reviewedPlaces = result.data,
                            sortType = sortType
                        ),
                    )
                }
            }
        }
    }

    fun collectPosition() = viewModelScope.launch {
        if (_mapUiState.value.currentPosition == null) {
            hLog("위치 트랙킹 시작")
            locationTracker.startTracking()
        }
    }
}

data class SearchUiState(
    val query: String = "",
    val chipStates: List<ChipState> = ChipState.default,
    val sortType: SortType = SortType.LATEST,
    val reviewedPlaces: List<ReviewedPlace> = emptyList()
)

data class PlaceDetailUiState(
    val detailPlace: DetailPlace? = null
)

data class MapUiState(
    var locationBound: LocationBound = LocationBound(),
    val searchUiState: SearchUiState = SearchUiState(),
    val placeDetailUiState: PlaceDetailUiState = PlaceDetailUiState(),
    val isFocusLocation: Boolean = false,
    val isShowBookmarks: Boolean = false,
    val currentPosition: Position? = null,
    val cameraPosition: Position? = null,
)

sealed interface MapUiEvent {
}
