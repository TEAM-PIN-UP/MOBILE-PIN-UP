package com.pinup.pinup.ui.findAccount.findId

import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.usecase.PostEmailVerifyUseCase
import com.pinup.pinup.domain.usecase.PostSendVerifyCodeUseCase
import com.pinup.pinup.domain.usecase.PostTemporaryPasswordUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.signup.EmailVerifyType
import com.pinup.pinup.util.Const
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FindIdViewModel(
    private val sendVerifyCodeUseCase: PostSendVerifyCodeUseCase,
    private val emailVerifyUseCase: PostEmailVerifyUseCase
): BaseViewModel<FindIdUiState, UiEvent>(
    FindIdUiState()
) {

    fun verifyEmail() = viewModelScope.launch {
        val request = EmailVerifyRequest(
            email = uiState.value.email,
            code = uiState.value.verificationCode
        )
        resultResponse(
            response = emailVerifyUseCase(request),
            successCallback = {
                handleEmailVerify(EmailVerifyType.VERIFIED)
            },
            errorCallback = {
                handleEmailVerify(EmailVerifyType.NOT_VERIFIED)
            }
        )
    }

    private fun handleEmailVerify(type: EmailVerifyType) {
        updateState {
            copy(
                emailVerifyType = type
            )
        }
    }

    fun onClickVerify() = viewModelScope.launch {
        if (isValidEmail()) {
            delayVerifyButton()
            val request = SendVerifyCodeRequest(
                email = uiState.value.email
            )
            resultResponse(
                response = sendVerifyCodeUseCase(request),
                successCallback = {}
            )
        }
    }

    private fun delayVerifyButton() = viewModelScope.launch {
        updateState {
            copy(
                isVerifyClicked = true
            )
        }
        delay(5000)
        updateState {
            copy(
                isVerifyClicked = false
            )
        }
    }

    fun updateEmail(email: String) {
        updateState {
            copy(
                email = email
            )
        }
    }

    fun updateVerificationCode(code: String) {
        updateState {
            copy(
                verificationCode = code
            )
        }
    }

    fun updateTabState(isFindByEmail: Boolean) {
        updateState {
            copy(
                isFindByEmail = isFindByEmail
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
                isEmailValid = isValid,
            )
        }
        return isValid
    }
}

data class FindIdUiState(
    val email: String = "",
    val verificationCode: String = "",
    val isEmailValid: Boolean = true,
    val isVerifyClicked: Boolean = false,
    val emailVerifyType: EmailVerifyType = EmailVerifyType.NONE,
    val nickName: String = "",
    val isFindByEmail: Boolean = true
) : UiState