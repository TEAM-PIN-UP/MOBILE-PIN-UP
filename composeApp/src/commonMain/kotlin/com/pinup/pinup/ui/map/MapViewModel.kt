package com.pinup.pinup.ui.map

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.CameraState
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.LocationBound
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.usecase.AddBookmarkUseCase
import com.pinup.pinup.domain.usecase.DeleteBookmarkUseCase
import com.pinup.pinup.domain.usecase.DeletePinlogUseCase
import com.pinup.pinup.domain.usecase.GetDetailPlaceUseCase
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
import com.pinup.pinup.domain.usecase.GetReviewedPlacesUseCase
import com.pinup.pinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.pinup.domain.usecase.SearchPlacesUseCase
import com.pinup.pinup.event.DetailPlaceEventBus
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.ui.model.ChipState
import dev.icerock.moko.geo.LocationTracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


class MapViewModel (
    private val getReviewedPlacesUseCase: GetReviewedPlacesUseCase,
    private val getDetailPlaceUseCase: GetDetailPlaceUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val searchPlacesUseCase: SearchPlacesUseCase,
    private val deletePinlogUseCase: DeletePinlogUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
    val locationTracker: LocationTracker
) : BaseViewModel<MapUiState, MapUiEvent>(MapUiState()) {
    init {
        initDetailPlaceEventBus()
        initCollectLocation()
        getMyProfileImage()
    }

    private fun initCollectLocation() = viewModelScope.launch {
        hLog("위치 수집 시작")
        locationTracker.getLocationsFlow()
            .distinctUntilChanged()
            .collectLatest {
                val myLocation = Position(it.latitude, it.longitude)
                hLog("myLocation >>> ${myLocation}")
                if (uiState.value.currentPosition == null || uiState.value.isFocusLocation) {
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
        updateState {
            copy(
                currentPosition = position
            )
        }
    }

    fun updateShowPinch() = viewModelScope.launch {
        clearDetailPlace()
        updateState {
            copy(
                pinchUiState = pinchUiState.copy(
                    pinchDetailList = if(isShowPinch) emptyList() else pinchUiState.pinchDetailList,
                ),
                isShowPinch = !isShowPinch
            )
        }
    }

    fun updateFocusLocation(value: Boolean) = viewModelScope.launch {
        updateState {
            copy(
                isFocusLocation = value
            )
        }

        if (value) {
            updateCameraPosition(uiState.value.currentPosition)
        }
    }

    private fun updateCameraPosition(position: Position?) = viewModelScope.launch {
        updateState {
            copy(
                cameraPosition = position
            )
        }
    }

    fun updateCameraState(cameraState: CameraState) {
        updateState {
            copy(
                cameraState = cameraState,
                isCameraMoving = cameraState.isMoving
            )
        }
    }

    fun getPlaces() = viewModelScope.launch {
        if (uiState.value.cameraState != null) {
            updateCameraPosition(uiState.value.cameraState!!.position)
            val latLngBounds = uiState.value.cameraState!!.contentBounds
            val request = uiState.value.locationBound.copy(
                neLatitude = latLngBounds.northEast.latitude.toString(),
                neLongitude = latLngBounds.northEast.longitude.toString(),
                swLatitude = latLngBounds.southWest.latitude.toString(),
                swLongitude = latLngBounds.southWest.longitude.toString(),
            )
            uiState.value.locationBound = request
            val filter = uiState.value.searchUiState.chipStates.find { it.isSelected }?.type ?: Category.ALL
            val sortType = uiState.value.searchUiState.sortType
            val currentLatLng = if (sortType == SortType.NEAR) uiState.value.currentPosition else null
            resultResponse(
                response = getReviewedPlacesUseCase(request, currentLatLng, sortType, filter),
                successCallback = {
                    updateState {
                        copy(
                            searchUiState = searchUiState.copy(
                                reviewedPlaces = it
                            )
                        )
                    }
                }
            )
        }
        if (uiState.value.cameraState?.reason == CameraState.Reason.GESTURE && uiState.value.isFocusLocation) {
            updateFocusLocation(false)
        }
    }

    fun updateSearchText(search: String) {
        getSearchedPlaceList(search)
        updateState {
            copy(
                searchUiState = searchUiState.copy(
                    query = search
                )
            )
        }
    }

    private fun getSearchedPlaceList(search: String) = viewModelScope.launch {
        resultResponse(
            response = searchPlacesUseCase(search),
            successCallback = { result ->
                updateState {
                    copy(
                        searchUiState = searchUiState.copy(
                            places = result
                        )
                    )
                }
            }
        )
    }

    fun updateChipState(chipState: ChipState) = viewModelScope.launch(Dispatchers.IO) {
        val request = uiState.value.locationBound
        val sortType = uiState.value.searchUiState.sortType
        val currentLatLng = if (sortType == SortType.NEAR) uiState.value.currentPosition else null
        resultResponse(
            response = getReviewedPlacesUseCase(request, currentLatLng, sortType, chipState.type),
            successCallback = {
                updateState {
                    copy(
                        searchUiState = searchUiState.copy(
                            reviewedPlaces = it,
                            chipStates = searchUiState.chipStates.map { chip ->
                                chip.copy(
                                    isSelected = chip == chipState
                                )
                            }
                        )
                    )
                }
            }
        )
    }

    fun getDetailPlace(kakaoPlaceId: String) = viewModelScope.launch {
        if (uiState.value.placeDetailUiState.detailPlace?.mapPlace?.kakaoPlaceId == kakaoPlaceId) return@launch
        resultResponse(
            response = getDetailPlaceUseCase(
                kakaoPlaceId = kakaoPlaceId,
                currentLatitude = uiState.value.currentPosition?.latitude?.toString(),
                currentLongitude = uiState.value.currentPosition?.longitude?.toString()
            ),
            successCallback = {
                updateState {
                    copy(
                        placeDetailUiState = PlaceDetailUiState(it),
                        cameraPosition = Position(it.mapPlace.latitude - 0.0078, it.mapPlace.longitude),
                        isFocusLocation = false,
                        isDetailClicked = true
                    )
                }
            }
        )
    }

    fun clearDetailPlace() = viewModelScope.launch {
        updateState {
            copy(
                placeDetailUiState = PlaceDetailUiState(),
                isDetailClicked = false
            )
        }
    }

    fun consumedDetailClicked() = viewModelScope.launch {
        updateState {
            copy(
                isDetailClicked = false
            )
        }
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
                        placeDetailUiState = placeDetailUiState.copy(
                            detailPlace = placeDetailUiState.detailPlace?.copy(
                                mapPlace = placeDetailUiState.detailPlace.mapPlace.copy(
                                    bookmark = placeDetailUiState.detailPlace.mapPlace.bookmark.not()
                                )
                            )
                        ),
                        searchUiState = searchUiState.copy(
                            reviewedPlaces = searchUiState.reviewedPlaces.map { reviewedPlace ->
                                if (reviewedPlace.kakaoPlaceId == kakaoPlaceId) {
                                    reviewedPlace.copy(bookmark = reviewedPlace.bookmark.not())
                                } else {
                                    reviewedPlace
                                }
                            }
                        )
                    )
                }
            }
        )
    }

    fun updateSortType(sortType: SortType) = viewModelScope.launch {
        val request = uiState.value.locationBound
        val chipState = uiState.value.searchUiState.chipStates.first { it.isSelected }
        val currentLatLng = if (sortType == SortType.NEAR) uiState.value.currentPosition else null
        resultResponse(
            response = getReviewedPlacesUseCase(request, currentLatLng, sortType, chipState.type),
            successCallback = {
                updateState {
                    copy(
                        searchUiState = searchUiState.copy(
                            reviewedPlaces = it,
                            sortType = sortType
                        ),
                    )
                }
            }
        )
    }

    fun collectPosition() = viewModelScope.launch {
        if (uiState.value.currentPosition == null) {
            hLog("위치 트랙킹 시작")
            locationTracker.startTracking()
        }
    }

    private fun getMyProfileImage() = viewModelScope.launch {
        getMyProfileUseCase().collectLatest {
            updateState {
                copy(
                    profileImage = it.profileUrl
                )
            }
        }
    }

    fun updateFocusSearch(focus: Boolean) {
        updateState {
            copy(
                searchUiState = searchUiState.copy(
                    isFocus = focus
                )
            )
        }
    }

    fun getPinchDetailList(id : Int) = viewModelScope.launch {
        //TODO 임시
        val request = uiState.value.locationBound
        val chipState = uiState.value.searchUiState.chipStates.first { it.isSelected }
        val currentLatLng = uiState.value.currentPosition
        resultResponse(
            response = getReviewedPlacesUseCase(request, currentLatLng, SortType.NEAR, chipState.type),
            successCallback = {
                updateState {
                    copy(
                        pinchUiState = pinchUiState.copy(
                            pinchDetailList = it
                        )
                    )
                }
            }
        )
    }

    fun clearPinchDetailList() {
        updateState {
            copy(
                pinchUiState = pinchUiState.copy(
                    pinchDetailList = emptyList()
                )
            )
        }
    }

    fun deleteReview(id: Int) = viewModelScope.launch {
        resultResponse(
            response = deletePinlogUseCase(id),
            successCallback = {
                uiState.value.placeDetailUiState.detailPlace?.mapPlace?.let {
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
                uiState.value.placeDetailUiState.detailPlace?.mapPlace?.let {
                    getDetailPlace(it.kakaoPlaceId)
                }
            }
        )
    }
}

data class SearchUiState(
    val query: String = "",
    val chipStates: List<ChipState> = ChipState.default,
    val sortType: SortType = SortType.LATEST,
    val reviewedPlaces: List<ReviewedPlace> = emptyList(),
    val isFocus: Boolean = false,
    val places: List<Place> = emptyList()
)

data class PlaceDetailUiState(
    val detailPlace: DetailPlace? = null
)

data class PinchUiState(
    val pinchList: List<PinchListItem> = emptyList(),
    val pinchDetailList : List<ReviewedPlace> = emptyList(),
)

data class MapUiState(
    var locationBound: LocationBound = LocationBound(),
    val searchUiState: SearchUiState = SearchUiState(),
    val placeDetailUiState: PlaceDetailUiState = PlaceDetailUiState(),
    val pinchUiState: PinchUiState = PinchUiState(),
    val isFocusLocation: Boolean = false,
    val isShowPinch: Boolean = false,
    val currentPosition: Position? = null,
    val cameraPosition: Position? = null,
    val isCameraMoving: Boolean = false,
    val isDetailClicked: Boolean = false,
    val cameraState: CameraState? = null,
    val profileImage: String = "",
) : UiState

sealed interface MapUiEvent : UiEvent {
    data object SuccessDelete : MapUiEvent
}
