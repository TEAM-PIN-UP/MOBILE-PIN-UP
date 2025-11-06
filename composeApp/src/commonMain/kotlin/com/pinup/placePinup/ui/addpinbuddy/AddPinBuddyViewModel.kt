package com.pinup.placePinup.ui.addpinbuddy

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.PinBuddy
import com.pinup.placePinup.domain.usecase.DeleteRecentPinBuddySearchUseCase
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.domain.usecase.GetRecentPinBuddySearchUseCase
import com.pinup.placePinup.domain.usecase.SaveRecentPinBuddySearchUseCase
import com.pinup.placePinup.domain.usecase.SearchUserUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AddPinBuddyViewModel (
    private val searchUserUseCase: SearchUserUseCase,
    private val getRecentSearchUseCase: GetRecentPinBuddySearchUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val saveRecentSearchUseCase: SaveRecentPinBuddySearchUseCase,
    private val deleteRecentSearchUseCase: DeleteRecentPinBuddySearchUseCase,
) : BaseViewModel<AddPinBuddyUiState, UiEvent>(AddPinBuddyUiState()) {

    init {
        getRecentSearchList()
        getMyInfo()
    }

    private fun getMyInfo() = viewModelScope.launch {
        getMyProfileUseCase()
            .collectLatest {
                updateState {
                    copy(
                        profileUrl = it.profileUrl,
                        myMemberId = it.memberId
                    )
                }
            }
    }

    fun updateQuery(inputText: String) = viewModelScope.launch {
        updateState {
            copy(
                query = inputText
            )
        }
    }

    fun search() = viewModelScope.launch {
        if (uiState.value.query.isEmpty()) return@launch
        saveRecentSearch()
        resultResponse(
            response = searchUserUseCase(uiState.value.query),
            successCallback = {
                updateState {
                    copy(
                        pinBuddies = it.filter { it.profile.memberId != uiState.value.myMemberId }
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
    val myMemberId: Int = -1,
    val query: String = "",
    val pinBuddies: List<PinBuddy>? = null,
    val recentSearchList: List<String> = emptyList(),
    val profileUrl: String = "",
) : UiState