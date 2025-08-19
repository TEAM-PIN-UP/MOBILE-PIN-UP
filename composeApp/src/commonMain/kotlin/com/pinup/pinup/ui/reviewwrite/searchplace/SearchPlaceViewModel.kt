package com.pinup.pinup.ui.reviewwrite.searchplace

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.usecase.SearchPlacesUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchPlaceViewModel (
    private val searchPlacesUseCase: SearchPlacesUseCase,
) :  BaseViewModel<SearchPlaceUiState, UiEvent>(SearchPlaceUiState()) {

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
        resultResponse(
            response = searchPlacesUseCase(query),
            successCallback = {
                updateState {
                    copy(
                        places = it
                    )
                }
            }
        )
    }
}

data class SearchPlaceUiState(
    val places: List<Place> = emptyList()
) : UiState