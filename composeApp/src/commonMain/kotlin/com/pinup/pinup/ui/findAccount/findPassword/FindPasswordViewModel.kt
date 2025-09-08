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
): BaseViewModel<FindPasswordUiState, UiEvent>(
    FindPasswordUiState()
) {
    fun updateEmail(email: String) = viewModelScope.launch {
        updateState {
            copy(
                email = email,
                isEmailValid = true
            )
        }
    }

    fun onClickSendPassword() = viewModelScope.launch {
        if (isValidEmail()) {
            val request = SendVerifyCodeRequest(
                email = uiState.value.email
            )
            resultResponse(
                response = postTemporaryPasswordUseCase(request),
                successCallback = {
                    updateState {
                        copy(
                            isSentPassword = true
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
        val isValid = uiState.value.email.isNotBlank() && uiState.value.email.matches(regex)
        updateState {
            copy(
                isEmailValid = isValid
            )
        }
        return isValid
    }

    fun onClickBack() {
        updateState {
            copy(
                isSentPassword = false
            )
        }
    }
}

data class FindPasswordUiState(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val isSentPassword: Boolean = false,
) : UiState