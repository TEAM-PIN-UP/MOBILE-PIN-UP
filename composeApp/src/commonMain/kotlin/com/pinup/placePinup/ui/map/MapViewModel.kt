package com.pinup.placePinup.ui.map

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.pints.GetEditorPintsRequest
import com.pinup.placePinup.domain.model.CameraState
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.DetailPlace
import com.pinup.placePinup.domain.model.EditorPintsDetail
import com.pinup.placePinup.domain.model.LocationBound
import com.pinup.placePinup.domain.model.PinchListItem
import com.pinup.placePinup.domain.model.PintsCategory
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.domain.model.Position.Companion.near
import com.pinup.placePinup.domain.model.ReviewedPlace
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.usecase.AddBookmarkUseCase
import com.pinup.placePinup.domain.usecase.DeleteBookmarkUseCase
import com.pinup.placePinup.domain.usecase.DeletePinlogUseCase
import com.pinup.placePinup.domain.usecase.GetDetailPlaceUseCase
import com.pinup.placePinup.domain.usecase.GetEditorPintsCategoryUseCase
import com.pinup.placePinup.domain.usecase.GetEditorPintsDetailUseCase
import com.pinup.placePinup.domain.usecase.GetEditorPintsUseCase
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.domain.usecase.GetReviewedPlacesUseCase
import com.pinup.placePinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.placePinup.domain.usecase.SearchPlacesUseCase
import com.pinup.placePinup.event.DetailPlaceEventBus
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.model.ChipState
import com.pinup.placePinup.util.MapZoom
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
    private val getEditorPintsUseCase: GetEditorPintsUseCase,
    private val getEditorPintsCategoryUseCase: GetEditorPintsCategoryUseCase,
    private val getEditorPintsDetailUseCase: GetEditorPintsDetailUseCase,
    private val postReviewLikeChangeUseCase: PostReviewLikeChangeUseCase,
    val locationTracker: LocationTracker
) : BaseViewModel<MapUiState, MapUiEvent>(MapUiState()) {

    private var initPlace: Boolean = false

    init {
        initDetailPlaceEventBus()
        initCollectLocation()
        getEditorPintsCategory()
    }

    private fun initCollectLocation() = viewModelScope.launch {
        locationTracker.getLocationsFlow()
            .distinctUntilChanged()
            .collectLatest {
                val myLocation = Position(it.latitude, it.longitude)
                // 특정 장소를 이미 센터링한 상태라면 최초 내 위치 센터링으로 덮어쓰지 않는다.
                val hasFocusedPlace = uiState.value.placeDetailUiState.detailPlace != null
                if ((uiState.value.currentPosition == null && !hasFocusedPlace) || uiState.value.isFocusLocation) {
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

    private fun updatePosition(position: Position) {
        initPlace = true
        updateState {
            copy(
                currentPosition = position
            )
        }
    }

    fun updateShowPinch() {
        clearDetailPlace()
        clearPinchDetailList()
        updateState {
            copy(
                isShowPinch = !isShowPinch
            )
        }
    }

    fun updateFocusLocation(value: Boolean) {
        updateState {
            copy(
                isFocusLocation = value
            )
        }

        if (value) {
            updateCameraPosition(uiState.value.currentPosition)
        }
    }

    private fun updateCameraPosition(position: Position?) {
        updateState {
            copy(
                cameraPosition = position,
                cameraZoom = null
            )
        }
    }

    fun updateCameraState(cameraState: CameraState) {
        updateState {
            copy(
                cameraState = cameraState,
                // 바텀시트 접힘은 사용자가 직접 지도를 움직였을 때만.
                // Follow 모드의 GPS 지터로 인한 미세 카메라 이동에는 상세 시트가 닫히지 않도록 제스처로 한정.
                isCameraMoving = cameraState.isMoving && cameraState.reason == CameraState.Reason.GESTURE
            )
        }

        if (uiState.value.currentPosition?.near(cameraState.position, 10.0) == true && initPlace) {
            getPlaces()
            getEditorPints()
            initPlace = false
            return
        }

        if (!cameraState.isMoving && uiState.value.needsMapRefresh) {
            updateState { copy(needsMapRefresh = false) }
            getPlaces()
        }
    }

    fun getPlaces() = viewModelScope.launch {
        if (uiState.value.isShowPinch) {
            getEditorPints()
        }
        if (uiState.value.cameraState != null) {
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

    private fun getEditorPintsCategory() = viewModelScope.launch {
        resultResponse(
            response = getEditorPintsCategoryUseCase(),
            successCallback = {
                updateState {
                    copy(
                        pinchUiState = pinchUiState.copy(
                            pintsCategoryList = it.map { category ->
                                ChipState(
                                    icon = null,
                                    text = category.kor,
                                    isSelected = category == PintsCategory.ALL,
                                    type = Category.NONE
                                )
                            }
                        )
                    )
                }
            }
        )
    }

    private fun getEditorPints(category: PintsCategory = PintsCategory.ALL) = viewModelScope.launch {
        val latLngBounds = uiState.value.cameraState!!.contentBounds
        val request = GetEditorPintsRequest(
            neLatitude = latLngBounds.northEast.latitude.toString(),
            neLongitude = latLngBounds.northEast.longitude.toString(),
            swLatitude = latLngBounds.southWest.latitude.toString(),
            swLongitude = latLngBounds.southWest.longitude.toString(),
            category = category
        )
        resultResponse(
            response = getEditorPintsUseCase(request),
            successCallback = {
                updateState {
                    copy(
                        pinchUiState = pinchUiState.copy(
                            pinchList = it
                        )
                    )
                }
            }
        )
    }

    fun updatePintsChipState(chipState: ChipState) {
        getEditorPints(PintsCategory.valuesOf(chipState.text))
        updateState {
            copy(
                pinchUiState = pinchUiState.copy(
                    pintsCategoryList = pinchUiState.pintsCategoryList.map { chip ->
                        chip.copy(
                            isSelected = chip == chipState
                        )
                    }
                )
            )
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
                        // 카메라를 장소보다 남쪽으로 살짝 내려, 핀이 하단 상세 시트 위 영역에 오도록 한다.
                        // 이 오프셋은 PLACE_FOCUS(줌16, 80dp=100m) 기준 약 220m. (기존 0.0078은 줌14 기준이라
                        //  줌16에서는 핀이 화면 밖으로 밀려 "엉뚱한 곳"으로 보이던 문제를 바로잡음)
                        cameraPosition = Position(it.mapPlace.latitude - 0.002, it.mapPlace.longitude),
                        // 해당 장소가 클러스터에 묶이지 않고 개별 핀으로 보이는 축척까지 확대한다.
                        cameraZoom = MapZoom.PLACE_FOCUS,
                        isFocusLocation = false,
                        isDetailClicked = true
                    )
                }
            }
        )
    }

    fun clearDetailPlace() {
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
            locationTracker.startTracking()
        }
    }

    fun getMyProfileImage() = viewModelScope.launch {
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

    fun updateSearchMode() {
        val exitingSearch = uiState.value.isSearchMode
        updateState {
            copy(
                isSearchMode = !isSearchMode,
                needsMapRefresh = exitingSearch,
                searchUiState = if (isSearchMode) searchUiState.copy(query = "") else searchUiState
            )
        }
    }

    fun getPinchDetailList(id : Int) = viewModelScope.launch {
        resultResponse(
            response = getEditorPintsDetailUseCase(id),
            successCallback = {
                updateState {
                    copy(
                        cameraPosition = Position(it.pintsPlaceList[0].latitude - 0.0078, it.pintsPlaceList[0].longitude),
                        isFocusLocation = false,
                        isDetailClicked = true,
                        pinchUiState = pinchUiState.copy(
                            editorPintsDetail = it
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
                    editorPintsDetail = EditorPintsDetail()
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
                hLog(uiState.value.placeDetailUiState.detailPlace.toString())
                uiState.value.placeDetailUiState.detailPlace?.mapPlace?.let {
                    getDetailPlace(it.kakaoPlaceId)
                }
            }
        )
    }

    fun onProfileClick(name: String) = viewModelScope.launch {
        getMyProfileUseCase().collectLatest {
            if(it.nickname != name) emitEvent(MapUiEvent.OnMoveUserProfile(name))
        }
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
    val editorPintsDetail: EditorPintsDetail = EditorPintsDetail(),
    val pintsCategoryList: List<ChipState> = emptyList(),
)

data class MapUiState(
    var locationBound: LocationBound = LocationBound(),
    val isSearchMode: Boolean = false,
    val needsMapRefresh: Boolean = false,
    val searchUiState: SearchUiState = SearchUiState(),
    val placeDetailUiState: PlaceDetailUiState = PlaceDetailUiState(),
    val pinchUiState: PinchUiState = PinchUiState(),
    val isFocusLocation: Boolean = false,
    val isShowPinch: Boolean = false,
    val currentPosition: Position? = null,
    val cameraPosition: Position? = null,
    /** 카메라 이동 시 보장할 최소 줌. null 이면 현재 줌을 유지한다. */
    val cameraZoom: Double? = null,
    val isCameraMoving: Boolean = false,
    val isDetailClicked: Boolean = false,
    val cameraState: CameraState? = null,
    val profileImage: String = "",
) : UiState

sealed interface MapUiEvent : UiEvent {
    data object SuccessDelete : MapUiEvent
    data class OnMoveUserProfile(val name: String): MapUiEvent
}
