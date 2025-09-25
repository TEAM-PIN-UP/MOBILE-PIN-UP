package com.pinup.pinup.ui.main

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainViewModel (
    private val getMyProfileUseCase: GetMyProfileUseCase
): BaseViewModel<MainUiState, UiEvent>(MainUiState()) {
    init {
        getProfileImage()
    }

    private fun getProfileImage() = viewModelScope.launch {
        getMyProfileUseCase()
            .collectLatest { profile ->
                updateState {
                    copy(
                        myId = profile.memberId,
                        profileImage = profile.profileUrl
                    )
                }
            }
    }
}

data class MainUiState(
    val myId: Int = -1,
    val profileImage: String = ""
): UiState