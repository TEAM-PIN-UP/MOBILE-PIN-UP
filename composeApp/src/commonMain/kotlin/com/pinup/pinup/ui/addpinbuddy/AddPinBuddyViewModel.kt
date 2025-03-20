package com.pinup.pinup.ui.addpinbuddy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.usecase.SearchUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class AddPinBuddyViewModel (
    private val searchUserUseCase: SearchUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddPinBuddyUiState())
    val uiState: StateFlow<AddPinBuddyUiState>
        get() = _uiState.asStateFlow()

    fun updateQuery(inputText: String) = viewModelScope.launch {
        _uiState.update {
            it.copy(
                query = inputText
            )
        }
    }

    fun search(query: String) = viewModelScope.launch {
        when (val result = searchUserUseCase(query)) {
            is PResult.Fail -> {

            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        pinBuddies = result.data
                    )
                }
            }
        }
    }
}

data class AddPinBuddyUiState(
    val query: String = "",
    val pinBuddies: List<PinBuddy>? = null
)