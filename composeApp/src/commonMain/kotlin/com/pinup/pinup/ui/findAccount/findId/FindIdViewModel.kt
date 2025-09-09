package com.pinup.pinup.ui.findAccount.findId

import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.usecase.CheckNickNameUseCase
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
    private val emailVerifyUseCase: PostEmailVerifyUseCase,
    private val checkNickNameUseCase: CheckNickNameUseCase
): BaseViewModel<FindIdUiState, FindIdUiEvent>(
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
                findIdByEmail()
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

    fun updateNickName(nickName: String) {
        updateState {
            copy(
                nickName = nickName
            )
        }
        checkNickName(nickName)
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

    private fun findIdByEmail() {
        //TODO 만약 성공한다면
        emitEvent(FindIdUiEvent.ShowInfoBottomSheet)
    }

    private fun findIdByNickName() {
        //TODO 만약 성공한다면
        updateState {
            copy(
                isShowInfoPage = true
            )
        }
    }

    fun onBackPressed() {
        updateState {
            copy(
                isShowInfoPage = false
            )
        }
    }

    private fun checkNickName(nickname: String) = viewModelScope.launch {
        handleSuccessCheckNickName(false)
        resultResponse(
            response = checkNickNameUseCase(nickname),
            successCallback = ::handleSuccessCheckNickName
        )
    }

    private fun handleSuccessCheckNickName(isNicknameUsed: Boolean) {
        updateState {
            copy(
                isNicknameUsed = isNicknameUsed
            )
        }
    }
}

data class FindIdUiState(
    val email: String = "",
    val verificationCode: String = "",
    val isEmailValid: Boolean = true,
    val isVerifyClicked: Boolean = false,
    val emailVerifyType: EmailVerifyType = EmailVerifyType.NONE,
    val nickName: String = "",
    val isNicknameUsed: Boolean = false,
    val isFindByEmail: Boolean = true,
    val isShowInfoPage: Boolean = false,
    val findEmail: String = "",
    val findNickName: String = "",
    val findProfileUrl: String = "",
) : UiState

sealed interface FindIdUiEvent: UiEvent {
    data object ShowInfoBottomSheet: FindIdUiEvent
}