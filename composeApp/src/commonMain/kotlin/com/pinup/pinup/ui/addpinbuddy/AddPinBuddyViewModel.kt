package com.pinup.pinup.ui.addpinbuddy

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.usecase.DeleteRecentPinBuddySearchUseCase
import com.pinup.pinup.domain.usecase.GetRecentPinBuddySearchUseCase
import com.pinup.pinup.domain.usecase.SaveRecentPinBuddySearchUseCase
import com.pinup.pinup.domain.usecase.SearchUserUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AddPinBuddyViewModel (
    private val searchUserUseCase: SearchUserUseCase,
    private val getRecentSearchUseCase: GetRecentPinBuddySearchUseCase,
    private val saveRecentSearchUseCase: SaveRecentPinBuddySearchUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentPinBuddySearchUseCase
) : BaseViewModel<AddPinBuddyUiState, UiEvent>(AddPinBuddyUiState()) {

    init {
        getRecentSearchList()
    }

    fun updateQuery(inputText: String) = viewModelScope.launch {
        updateState {
            copy(
                query = inputText
            )
        }
    }

    fun search() = viewModelScope.launch {
        saveRecentSearch()
        resultResponse(
            response = searchUserUseCase(uiState.value.query),
            successCallback = {
                updateState {
                    copy(
                        pinBuddies = it
                    )
                }
            }
        )
    }

    private fun getRecentSearchList() = viewModelScope.launch {
        getRecentSearchUseCase()
            .collectLatest {
                updateState {
                    copy(
                        recentSearchList = it
                    )
                }
            }
    }

    fun deleteRecentSearch(index: Int) = viewModelScope.launch {
        deleteRecentSearchUseCase(index)
    }

    private fun saveRecentSearch() = viewModelScope.launch {
        saveRecentSearchUseCase(uiState.value.query)
    }
}

data class AddPinBuddyUiState(
    val query: String = "",
    val pinBuddies: List<PinBuddy>? = null,
    val recentSearchList: List<String> = emptyList()
) : UiState