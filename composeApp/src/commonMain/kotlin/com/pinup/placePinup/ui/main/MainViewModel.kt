package com.pinup.placePinup.ui.main

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
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