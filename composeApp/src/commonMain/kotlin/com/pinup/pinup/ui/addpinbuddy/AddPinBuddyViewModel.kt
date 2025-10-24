package com.pinup.pinup.ui.addpinbuddy

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.usecase.DeleteRecentPinBuddySearchUseCase
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
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