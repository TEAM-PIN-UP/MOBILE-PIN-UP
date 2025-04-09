package com.pinup.pinup.ui.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.usecase.DeleteBookmarkUseCase
import com.pinup.pinup.domain.usecase.GetBookmarksUseCase
import com.pinup.pinup.ui.model.ChipState
import dev.icerock.moko.geo.LocationTracker
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.location.LOCATION
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class BookmarkViewModel(
    private val getBookmarksUseCase: GetBookmarksUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    val locationTracker: LocationTracker
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState>
        get() = _uiState.asStateFlow()

    init {
        initLocation()
    }

    private fun initLocation() = viewModelScope.launch {
        if (locationTracker.permissionsController.isPermissionGranted(Permission.LOCATION)) {
            locationTracker.startTracking()
            locationTracker.getLocationsFlow()
                .onEach {
                    _uiState.value = uiState.value.copy(
                        currentLatitude = it.latitude.toString(),
                        currentLongitude = it.longitude.toString(),
                    )
                }
                .launchIn(this)
        }
    }

    fun initBookmarkedPlaces() = viewModelScope.launch {
        when (val result = getBookmarksUseCase(
            sort = _uiState.value.sortType,
            category = _uiState.value.chipStates.first { it.isSelected }.type,
            currentLatitude = if (_uiState.value.isNear) _uiState.value.currentLatitude.toString() else "",
            currentLongitude = if (_uiState.value.isNear) _uiState.value.currentLongitude.toString() else "",
        )
        ) {
            is PResult.Fail -> {}
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        bookmarkedPlace = result.data,
                        permissionState = locationTracker.permissionsController.isPermissionGranted(
                            Permission.LOCATION
                        )
                    )
                }
            }
        }
    }

    fun updateFilterCategory(chipState: ChipState) = viewModelScope.launch {
        when (val result = getBookmarksUseCase(
            sort = _uiState.value.sortType,
            category = chipState.type,
            currentLatitude = if (_uiState.value.isNear) _uiState.value.currentLatitude.toString() else "",
            currentLongitude = if (_uiState.value.isNear) _uiState.value.currentLongitude.toString() else "",
        )
        ) {
            is PResult.Fail -> {}
            is PResult.Success -> {
                _uiState.update { uiState ->
                    uiState.copy(
                        bookmarkedPlace = result.data,
                        chipStates = _uiState.value.chipStates.map {
                            it.copy(
                                isSelected = it == chipState
                            )
                        }
                    )
                }
            }
        }
    }

    fun updateSortType(sortType: SortType) = viewModelScope.launch {
        when (val result = getBookmarksUseCase(
            sort = sortType,
            category = _uiState.value.chipStates.first { it.isSelected }.type,
            currentLatitude = if (_uiState.value.isNear) _uiState.value.currentLatitude
                ?: "" else "",
            currentLongitude = if (_uiState.value.isNear) _uiState.value.currentLongitude
                ?: "" else "",
        )
        ) {
            is PResult.Fail -> {}
            is PResult.Success -> {
                _uiState.update { uiState ->
                    uiState.copy(
                        bookmarkedPlace = result.data,
                        sortType = sortType,
                    )
                }
            }
        }
    }

    fun updateBookmark(kakaoPlaceId: String) = viewModelScope.launch {
        when (deleteBookmarkUseCase(kakaoPlaceId)) {
            is PResult.Fail -> {}
            is PResult.Success -> {
                val removeItem =
                    _uiState.value.bookmarkedPlace.find { it.kakaoPlaceId == kakaoPlaceId }
                val updatedBookmarkedPlaces = _uiState.value.bookmarkedPlace
                    .toMutableList()
                updatedBookmarkedPlaces.remove(removeItem)
                _uiState.update { uiState ->
                    uiState.copy(
                        bookmarkedPlace = updatedBookmarkedPlaces,
                    )
                }
            }
        }
    }
}

data class BookmarkUiState(
    val bookmarkedPlace: List<BookmarkedPlace> = emptyList(),
    val sortType: SortType = SortType.LATEST,
    val chipStates: List<ChipState> = ChipState.default,
    val permissionState: Boolean = false,
    val currentLatitude: String? = null,
    val currentLongitude: String? = null,
) {
    val isNear = sortType == SortType.NEAR
}