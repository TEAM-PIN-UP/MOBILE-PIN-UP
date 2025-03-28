package com.pinup.pinup.ui.reviewwrite.searchplace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.usecase.SearchPlacesUseCase
import com.pinup.pinup.hLog
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)

class SearchPlaceViewModel (
    private val searchPlacesUseCase: SearchPlacesUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchPlaceUiState())
    val uiState: StateFlow<SearchPlaceUiState>
        get() = _uiState.asStateFlow()
    val query: MutableStateFlow<String> = MutableStateFlow("")

    init {
        viewModelScope.launch {
            query
                .filter {
                    it.isNotBlank()
                }
                .debounce(200)
                .collectLatest {
                    searchPlace(it)
                }
        }
    }

    fun updateQuery(inputText: String) {
        query.value = inputText
    }

    private fun searchPlace(query: String) = viewModelScope.launch {
        when (val result = searchPlacesUseCase(query)) {
            is PResult.Fail -> {
                hLog("fail >> ${result.failState}")
            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        places = result.data
                    )
                }
            }
        }
    }
}

data class SearchPlaceUiState(
    val places: List<Place> = emptyList()
)