package com.pinup.placePinup.ui.findAccount.changePassword

import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.ChangePasswordRequest
import com.pinup.placePinup.domain.usecase.ChangePasswordUseCase
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.util.Const
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val getMyProfileUseCase: GetMyProfileUseCase
): BaseViewModel<ChangePasswordUiState, ChangePasswordUiEvent>(
    ChangePasswordUiState()
) {

    init {
        updateMyProfile()
    }

    private fun updateMyProfile() = viewModelScope.launch {
        getMyProfileUseCase().collectLatest {
            updateState {
                copy(
                    email = it.email
                )
            }
        }
    }

    fun updatePassword(password: String) = viewModelScope.launch {
        val regex = Regex(Const.PRegex.PASSWORD_REGEX)
        updateState {
            copy(
                password = password,
                isPasswordValid = password.isNotBlank() && password.matches(regex)
            )
        }
    }

    fun updatePasswordAgain(password: String) = viewModelScope.launch {
        updateState {
            copy(
                passwordAgain = password,
                isPasswordMatched = uiState.value.password == password
            )
        }
    }

    fun onClickShowFirstPassword() = viewModelScope.launch {
        updateState {
            copy(
                isShowFirstPassword = !uiState.value.isShowFirstPassword,
            )
        }
    }

    fun onClickShowPassword() = viewModelScope.launch {
        updateState {
            copy(
                isShowPassword = !uiState.value.isShowPassword,
            )
        }
    }

    fun changePassword() = viewModelScope.launch {
        val request = ChangePasswordRequest(
            email = uiState.value.email,
            password = uiState.value.password
        )
        resultResponse(
            response = changePasswordUseCase(request),
            successCallback = {
                emitEvent(ChangePasswordUiEvent.MoveLogin)
            }
        )
    }
}

data class ChangePasswordUiState(
    val email: String = "",
    val password: String = "",
    val passwordAgain: String = "",
    val isPasswordValid: Boolean = true,
    val isPasswordMatched: Boolean = true,
    val isShowFirstPassword: Boolean = false,
    val isShowPassword: Boolean = false,
) : UiState

sealed interface ChangePasswordUiEvent : UiEvent {
    data object MoveLogin : ChangePasswordUiEvent
}
