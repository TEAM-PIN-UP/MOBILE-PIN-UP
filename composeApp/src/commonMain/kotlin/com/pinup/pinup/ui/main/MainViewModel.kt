package com.pinup.pinup.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel (
    private val getMyProfileUseCase: GetMyProfileUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState>
        get() = _uiState.asStateFlow()

    init {
        getProfileImage()
    }

    private fun getProfileImage() = viewModelScope.launch {
        getMyProfileUseCase()
            .collectLatest { profile ->
                _uiState.update {
                    it.copy(
                        profileImage = profile.profileUrl
                    )
                }
            }
    }
}

data class MainUiState(
    val profileImage: String = ""
)