package com.pinup.placePinup.ui.setting

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.domain.usecase.LogoutUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.login.model.SNSType
import kotlinx.coroutines.launch


class SettingViewModel (
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : BaseViewModel<SettingUiState, SettingUiEvent>(SettingUiState()) {

    init {
        getProfile()
    }

    private fun getProfile() = viewModelScope.launch {
        val profile = getMyProfileUseCase().value
        updateState {
            copy(
                snsType = profile.snsType,
                email = profile.email
            )
        }
    }

    fun logout() = viewModelScope.launch {
        logoutUseCase()
        emitEvent(SettingUiEvent.MoveLogin)
    }
}

data class SettingUiState(
    val snsType: SNSType? = null,
    val email: String = ""
) : UiState

sealed interface SettingUiEvent : UiEvent {
    data object MoveLogin : SettingUiEvent
}
