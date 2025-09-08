package com.pinup.pinup.ui.findAccount.findPassword

import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.usecase.PostTemporaryPasswordUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.util.Const
import kotlinx.coroutines.launch

class FindPasswordViewModel(
    private val postTemporaryPasswordUseCase: PostTemporaryPasswordUseCase
): BaseViewModel<ChangePasswordUiState, UiEvent>(
    ChangePasswordUiState()
) {
    fun updateEmail(email: String) = viewModelScope.launch {
        updateState {
            copy(
                emailState = uiState.value.emailState.copy(
                    email = email,
                    isEmailValid = true
                )
            )
        }
    }

    fun onClickSendPassword() = viewModelScope.launch {
        if (isValidEmail()) {
            val request = SendVerifyCodeRequest(
                email = uiState.value.emailState.email
            )
            resultResponse(
                response = postTemporaryPasswordUseCase(request),
                successCallback = {
                    updateState {
                        copy(
                            emailState = emailState.copy(
                                isSentPassword = true
                            )
                        )
                    }
                }
            )
        }
    }

    fun isValidEmail(): Boolean {
        val regex = Regex(
            pattern = Const.PRegex.EMAIL_REGEX,
            option = RegexOption.IGNORE_CASE
        )
        val isValid = uiState.value.emailState.email.isNotBlank() && uiState.value.emailState.email.matches(regex)
        updateState {
            copy(
                emailState = emailState.copy(
                    isEmailValid = isValid,
                )
            )
        }
        return isValid
    }

    fun updatePassword(password: String) = viewModelScope.launch {
        val regex = Regex(Const.PRegex.PASSWORD_REGEX)
        updateState {
            copy(
                passwordState = passwordState.copy(
                    password = password,
                    isPasswordValid = password.isNotBlank() && password.matches(regex)
                )
            )
        }
    }

    fun updatePasswordAgain(password: String) = viewModelScope.launch {
        updateState {
            copy(
                passwordState = uiState.value.passwordState.copy(
                    passwordAgain = password,
                    isPasswordMatched = password == passwordState.password
                )
            )
        }
    }

    fun onClickShowPassword() = viewModelScope.launch {
        updateState {
            copy(
                passwordState = uiState.value.passwordState.copy(
                    isShowPassword = !uiState.value.passwordState.isShowPassword,
                )
            )
        }
    }

    fun onClickBack() {
        updateState {
            copy(
                emailState = emailState.copy(
                    isSentPassword = false
                )
            )
        }
    }
}

data class ChangePasswordUiState(
    val emailState: ChangePasswordEmailState = ChangePasswordEmailState(),
    val passwordState: ChangePasswordState = ChangePasswordState()
) : UiState

data class ChangePasswordEmailState(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val isSentPassword: Boolean = false,
)

data class ChangePasswordState(
    val password: String = "",
    val passwordAgain : String = "",
    val isPasswordValid: Boolean = true,
    val isPasswordMatched: Boolean = true,
    val isShowPassword: Boolean = false,
) {
    val isPassValidation = isPasswordValid && isPasswordMatched && password.isNotEmpty() && passwordAgain.isNotEmpty()
}