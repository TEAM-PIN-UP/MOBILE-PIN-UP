package com.pinup.placePinup.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class MainViewModel (
    savedStateHandle: SavedStateHandle,
    private val getMyProfileUseCase: GetMyProfileUseCase
): BaseViewModel<MainUiState, UiEvent>(MainUiState()) {
    init {
        getProfileImage()
    }

    companion object {
        private const val IS_MY_PAGE = "isMyPage"

    }

    var isMyPage = savedStateHandle.get<Boolean>(IS_MY_PAGE) ?: false

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