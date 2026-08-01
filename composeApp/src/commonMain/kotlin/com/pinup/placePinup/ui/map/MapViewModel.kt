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

    // 피드 뱃지 등으로 진입 시, 카메라가 대상 장소에 안착하면 그 지역 장소 목록을 강제로 로드하기 위한 예약 플래그.
    // (내 위치 근접 게이트를 우회 → 멀리 있는 장소로 진입해도 목록/마커가 채워지고, 상세를 닫아도 지도가 비지 않음)
    private var forceLoadPlaces: Boolean = false

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
                // 피드 등 외부 진입: 대상 지역 장소 목록을 강제 로드하도록 예약 후 상세 조회.
                forceLoadPlaces = true
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
                cameraPosition = position
            )
        }
    }

    fun updateCameraState(cameraState: CameraState) {
        updateState {
            copy(
                cameraState = cameraState,
                // 시트 접힘(hidden)은 "사용자가 제스처로 지도를 움직였을 때"만 의도된 동작이다.
                // Follow 모드의 GPS 추적이나 상세 진입 등 프로그램적 카메라 이동으로는 접히지 않도록 제스처 이동만 반영.
                isCameraMoving = cameraState.isMoving && cameraState.reason == CameraState.Reason.GESTURE
            )
        }

        // 피드 진입 로드가 진행 중이면(forceLoadPlaces) 근접 게이트 로드가 그 결과를 덮어쓰지 않도록 건너뛴다.
        if (!forceLoadPlaces && uiState.value.currentPosition?.near(cameraState.position, 10.0) == true && initPlace) {
            getPlaces()
            getEditorPints()
            initPlace = false
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
                        cameraPosition = Position(it.mapPlace.latitude - 0.0078, it.mapPlace.longitude),
                        isFocusLocation = false,
                        isDetailClicked = true
                    )
                }
                // 피드 등 외부 진입인 경우, 대상 지역 장소를 로드해 목록/마커를 채운다.
                // (forceLoadPlaces 는 로드 완료 후 loadReviewedPlacesAround 내부에서 해제)
                if (forceLoadPlaces) {
                    loadReviewedPlacesAround(it.mapPlace)
                }
            }
        )
    }

    // 대상 장소 주변(고정 박스)의 리뷰 장소를 로드해 지도 마커/바텀시트 목록을 채운다.
    // 로드 결과에 대상이 없거나(필터/일시적 누락) 로드가 실패해도 대상 핀은 항상 보이도록 보장한다.
    private fun loadReviewedPlacesAround(target: ReviewedPlace) = viewModelScope.launch {
        // 1) 대상 장소를 먼저 표시해 선택 핀을 즉시 보장(로드 실패/지연에도 유지).
        updateState {
            copy(searchUiState = searchUiState.copy(reviewedPlaces = listOf(target)))
        }
        // 2) 대상 주변 지역 장소 로드.
        val delta = 0.02
        val request = uiState.value.locationBound.copy(
            neLatitude = (target.latitude + delta).toString(),
            neLongitude = (target.longitude + delta).toString(),
            swLatitude = (target.latitude - delta).toString(),
            swLongitude = (target.longitude - delta).toString(),
        )
        uiState.value.locationBound = request
        val filter = uiState.value.searchUiState.chipStates.find { it.isSelected }?.type ?: Category.ALL
        val sortType = uiState.value.searchUiState.sortType
        val currentLatLng = if (sortType == SortType.NEAR) uiState.value.currentPosition else null
        resultResponse(
            response = getReviewedPlacesUseCase(request, currentLatLng, sortType, filter),
            successCallback = { places ->
                // 주변 로드 결과에 대상이 없어도 항상 포함해 선택 핀을 유지.
                val merged = if (places.any { it.kakaoPlaceId == target.kakaoPlaceId }) places
                             else places + target
                updateState {
                    copy(searchUiState = searchUiState.copy(reviewedPlaces = merged))
                }
            }
        )
        // 3) 로드 완료(성공/실패) 후 근접 게이트 재허용.
        forceLoadPlaces = false
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
    data class OnMoveUserProfile(val name: String): MapUiEvent
}
