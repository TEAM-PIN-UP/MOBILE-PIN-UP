package com.pinup.pinup.ui.addpinbuddy

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.usecase.SearchUserUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch


class AddPinBuddyViewModel (
    private val searchUserUseCase: SearchUserUseCase
) : BaseViewModel<AddPinBuddyUiState, UiEvent>(AddPinBuddyUiState()) {

    fun updateQuery(inputText: String) = viewModelScope.launch {
        updateState {
            copy(
                query = inputText
            )
        }
    }

    fun search() = viewModelScope.launch {
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
}

data class AddPinBuddyUiState(
    val query: String = "",
    val pinBuddies: List<PinBuddy>? = null,
    val recentSearchList: List<String> = emptyList()
) : UiState