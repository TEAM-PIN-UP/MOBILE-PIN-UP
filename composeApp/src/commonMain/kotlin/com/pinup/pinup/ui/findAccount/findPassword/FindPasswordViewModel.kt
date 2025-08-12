package com.pinup.pinup.ui.findAccount.findPassword

import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.signup.EmailVerifyType
import com.pinup.pinup.util.Const
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FindPasswordViewModel: BaseViewModel<ChangePasswordUiState, UiEvent>(
    ChangePasswordUiState()
) {
    companion object {
        const val RETRY_VERIFICATION_TIME = 5000
    }
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

    fun updateVerificationCode(code: String) = viewModelScope.launch {
        //TODO 최대 글자 도달시 확인 api 로직 구현 현재는 6자리 시 통과 가능
        updateState {
            copy(
                emailState = uiState.value.emailState.copy(
                    verificationCode = code,
                    emailVerifyType = if(code.length == 6) EmailVerifyType.VERIFIED else EmailVerifyType.NOT_VERIFIED
                )
            )
        }
    }

    fun onClickSendCode() = viewModelScope.launch {
        if (isValidEmail()) {
            retryVerifyCount()
            //TODO: 이메일 인증 로직 구현
        }
    }

    fun onClickVerify() {
        //TODO: 이메일 인증 로직 구현
    }

    fun retryVerifyCount() {
        viewModelScope.launch {
            updateState {
                copy(
                    emailState = uiState.value.emailState.copy(
                        isClicked = true
                    )
                )
            }
            delay(RETRY_VERIFICATION_TIME.toLong())
            updateState {
                copy(
                    emailState = uiState.value.emailState.copy(
                        isClicked = false
                    )
                )
            }
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

    fun changePassword() {
        //TODO 서버 비밀번호 변경 로직
    }
}

data class ChangePasswordUiState(
    val emailState: ChangePasswordEmailState = ChangePasswordEmailState(),
    val passwordState: ChangePasswordState = ChangePasswordState()
) : UiState

data class ChangePasswordEmailState(
    val email: String = "",
    val verificationCode: String = "",
    val isEmailValid: Boolean = true,
    val emailVerifyType: EmailVerifyType = EmailVerifyType.NONE,
    val isClicked: Boolean = false,
) {
    val isPassValidation = isEmailValid && emailVerifyType == EmailVerifyType.VERIFIED
}

data class ChangePasswordState(
    val password: String = "",
    val passwordAgain : String = "",
    val isPasswordValid: Boolean = true,
    val isPasswordMatched: Boolean = true,
    val isShowPassword: Boolean = false,
) {
    val isPassValidation = isPasswordValid && isPasswordMatched && password.isNotEmpty() && passwordAgain.isNotEmpty()
}