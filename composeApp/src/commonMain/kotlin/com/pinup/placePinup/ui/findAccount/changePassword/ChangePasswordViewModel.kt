package com.pinup.placePinup.ui.findAccount.changePassword

import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.util.Const
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
): BaseViewModel<ChangePasswordUiState, ChangePasswordUiEvent>(
    ChangePasswordUiState()
) {
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
                isPasswordMatched = password == passwordAgain
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
        //TODO 비밀번호 변경
    }
}

data class ChangePasswordUiState(
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
