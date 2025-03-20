package com.pinup.pinup.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.usecase.GetMyProfileUseCase
import com.pinup.pinup.domain.usecase.LogoutUseCase
import com.pinup.pinup.ui.login.model.SNSType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class SettingViewModel (
    private val getMyProfileUseCase: GetMyProfileUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState>
        get() = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SettingUiEvent>()
    val uiEvent: SharedFlow<SettingUiEvent>
        get() = _uiEvent.asSharedFlow()

    init {
        getProfile()
    }

    private fun getProfile() = viewModelScope.launch {
        val profile = getMyProfileUseCase().value
        _uiState.update {
            it.copy(
                snsType = profile.snsType,
                email = profile.email
            )
        }
    }

    fun logout() = viewModelScope.launch {
        logoutUseCase()
        _uiEvent.emit(SettingUiEvent.MoveLogin)
    }
}

data class SettingUiState(
    val snsType: SNSType? = null,
    val email: String = ""
)

sealed interface SettingUiEvent {
    data object MoveLogin : SettingUiEvent
}
