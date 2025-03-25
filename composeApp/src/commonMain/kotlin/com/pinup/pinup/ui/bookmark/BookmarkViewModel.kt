package com.pinup.pinup.ui.bookmark

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.usecase.GetBookmarksUseCase
import com.pinup.pinup.ui.model.ChipState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class BookmarkViewModel (
    private val getBookmarksUseCase: GetBookmarksUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookmarkUiState())
    val uiState: StateFlow<BookmarkUiState>
        get() = _uiState.asStateFlow()
    private val locationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        initBookmarkedPlaces()
    }

    private fun getLastLocation(): Location? {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val result = locationProviderClient.lastLocation.result
            hLog("result >>> ${result}")
            return result
        } else {
            return null
        }
    }

    private fun initBookmarkedPlaces() = viewModelScope.launch {
        when (val result = getBookmarksUseCase(
            sort = _uiState.value.sortType,
            category = _uiState.value.chipStates.first{ it.isSelected }.type,
            currentLatitude = if (_uiState.value.isNear) getLastLocation()?.latitude.toString() else "",
            currentLongitude = if (_uiState.value.isNear) getLastLocation()?.longitude.toString() else "",
        )
        ) {
            is PResult.Fail -> {}
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        bookmarkedPlace = result.data
                    )
                }
            }
        }
    }

    fun updateFilterCategory(chipState: ChipState) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getBookmarksUseCase(
            sort = _uiState.value.sortType,
            category = chipState.type,
            currentLatitude = if (_uiState.value.isNear) getLastLocation()?.latitude.toString() else "",
            currentLongitude = if (_uiState.value.isNear) getLastLocation()?.longitude.toString() else "",
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

    fun updateSortType(sortType: SortType) = viewModelScope.launch(Dispatchers.IO) {
        when (val result = getBookmarksUseCase(
            sort = sortType,
            category = _uiState.value.chipStates.first{ it.isSelected }.type,
            currentLatitude = if (_uiState.value.isNear) getLastLocation()?.latitude.toString() else "",
            currentLongitude = if (_uiState.value.isNear) getLastLocation()?.longitude.toString() else "",
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
}

data class BookmarkUiState(
    val bookmarkedPlace: List<BookmarkedPlace> = emptyList(),
    val sortType: SortType = SortType.LATEST,
    val chipStates: List<ChipState> = ChipState.default
) {
    val isNear = sortType == SortType.NEAR
}