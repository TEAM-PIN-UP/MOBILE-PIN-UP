package com.pinup.pinup.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.compose.CameraPositionState
import com.naver.maps.map.compose.CameraUpdateReason
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.LocationBound
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.usecase.AddBookmarkUseCase
import com.pinup.pinup.domain.usecase.DeleteBookmarkUseCase
import com.pinup.pinup.domain.usecase.GetDetailPlaceUseCase
import com.pinup.pinup.domain.usecase.GetReviewedPlacesUseCase
import com.pinup.pinup.event.DetailPlaceEventBus
import com.pinup.pinup.hLog
import com.pinup.pinup.ui.model.ChipState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MapViewModel (
    private val getReviewedPlacesUseCase: GetReviewedPlacesUseCase,
    private val getDetailPlaceUseCase: GetDetailPlaceUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
) : ViewModel() {
    private val _mapUiState = MutableStateFlow(MapUiState())
    val mapUiState: StateFlow<MapUiState>
        get() = _mapUiState.asStateFlow()
    private val _uiEvent = MutableSharedFlow<MapUiEvent>()
    val uiEvent: SharedFlow<MapUiEvent>
        get() = _uiEvent.asSharedFlow()
    private val locationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        initDetailPlaceEventBus()
    }

    @OptIn(FlowPreview::class)
    private fun initDetailPlaceEventBus() = viewModelScope.launch {
        DetailPlaceEventBus.detailPlaceEvent
            .debounce(300)
            .collectLatest { kakaoId ->
                getDetailPlace(kakaoId)
            }
    }

    private fun updatePosition(position: LatLng) = viewModelScope.launch {
        _mapUiState.update {
            it.copy(
                currentLatLng = position
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
            updateCameraPosition(_mapUiState.value.currentLatLng)
        }
    }

    private fun updateCameraPosition(latLng: LatLng?) = viewModelScope.launch {
        hLog("updateCameraPosition >>> ${latLng}")
        _mapUiState.update {
            it.copy(
                cameraPosition = latLng
            )
        }
    }


    fun getPlaces(cameraPositionState: CameraPositionState) = viewModelScope.launch {
        if (cameraPositionState.isMoving.not()) {
            updateCameraPosition(cameraPositionState.position.target)
            val latLngBounds = cameraPositionState.contentBounds
            if (latLngBounds != null) {
                val request = _mapUiState.value.locationBound.copy(
                    neLatitude = latLngBounds.northEast.latitude.toString(),
                    neLongitude = latLngBounds.northEast.longitude.toString(),
                    swLatitude = latLngBounds.southWest.latitude.toString(),
                    swLongitude = latLngBounds.southWest.longitude.toString(),
                )
                _mapUiState.value.locationBound = request
                val filter = _mapUiState.value.searchUiState.chipStates.find { it.isSelected }?.type ?: Category.ALL
                val sortType = _mapUiState.value.searchUiState.sortType
                val currentLatLng = if (sortType == SortType.NEAR) _mapUiState.value.currentLatLng else null
                when (val result = getReviewedPlacesUseCase(request, currentLatLng, sortType, filter)) {
                    is PResult.Fail -> {
                        hLog( "fail >> ${result.failState}")
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
            }
        } else {
            if (cameraPositionState.cameraUpdateReason == CameraUpdateReason.GESTURE && _mapUiState.value.isFocusLocation) {
                hLog("3")
                updateFocusLocation(false)
            }
        }
    }

    fun updateChipState(chipState: ChipState) = viewModelScope.launch(Dispatchers.IO) {
        val request = _mapUiState.value.locationBound
        val sortType = _mapUiState.value.searchUiState.sortType
        val currentLatLng = if (sortType == SortType.NEAR) _mapUiState.value.currentLatLng else null
        when (val result = getReviewedPlacesUseCase(request, currentLatLng, sortType, chipState.type)) {
            is PResult.Fail -> {
                hLog( "fail >> ${result.failState}")
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
            currentLatitude = _mapUiState.value.currentLatLng?.latitude?.toString(),
            currentLongitude = _mapUiState.value.currentLatLng?.longitude?.toString()
        )) {
            is PResult.Fail -> {
                hLog( "fail >> ${result.failState}")
            }

            is PResult.Success -> {
                _mapUiState.update {
                    it.copy(
                        placeDetailUiState = PlaceDetailUiState(result.data),
                        cameraPosition = LatLng(result.data.mapPlace.latitude, result.data.mapPlace.longitude),
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
                hLog( "fail >> ${result.failState}")
            }
            is PResult.Success -> {
                hLog( "success >")
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
        val currentLatLng = if (sortType == SortType.NEAR) _mapUiState.value.currentLatLng else null
        when (val result = getReviewedPlacesUseCase(request, currentLatLng, sortType, chipState.type)) {
            is PResult.Fail -> {
                hLog( "fail >> ${result.failState}")
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

    fun collectPosition() {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && _mapUiState.value.currentLatLng == null
        ) {
            locationProviderClient.requestLocationUpdates(
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5_000)
                    .setIntervalMillis(5_000).build(),
                { location ->
                    val myLocation = LatLng(location.latitude, location.longitude)
                    hLog( "myLocation >>> ${myLocation}")
                    if (_mapUiState.value.currentLatLng == null || _mapUiState.value.isFocusLocation) {
                        updateCameraPosition(myLocation)
                    }
                    updatePosition(myLocation)
                },
                Looper.getMainLooper()
            )
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
    val currentLatLng: LatLng? = null,
    val cameraPosition: LatLng? = null,
)

sealed interface MapUiEvent {
}
