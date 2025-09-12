package com.pinup.pinup.ui.findAccount.findId

import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.data.request.findId.FindByEmailRequest
import com.pinup.pinup.data.request.findId.FindByNickNameRequest
import com.pinup.pinup.domain.usecase.CheckNickNameUseCase
import com.pinup.pinup.domain.usecase.GetFindIdByEmailUseCase
import com.pinup.pinup.domain.usecase.GetFindIdByNicknameUseCase
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
    private val checkNickNameUseCase: CheckNickNameUseCase,
    private val getFindIdByEmailUseCase: GetFindIdByEmailUseCase,
    private val getFindIdByNicknameUseCase: GetFindIdByNicknameUseCase
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

    private fun findIdByEmail() = viewModelScope.launch {
        val request = FindByEmailRequest(uiState.value.email)
        resultResponse(
            response = getFindIdByEmailUseCase(request),
            successCallback = {
                updateState {
                    copy(
                        findEmail = it.email,
                        findNickName = it.nickname,
                        findProfileUrl = it.profileUrl
                    )
                }
                emitEvent(FindIdUiEvent.ShowInfoBottomSheet)
            }
        )
    }

    private fun findIdByNickName() = viewModelScope.launch {
        val request = FindByNickNameRequest(uiState.value.nickName)
        resultResponse(
            response = getFindIdByNicknameUseCase(request),
            successCallback = {
                updateState {
                    copy(
                        findEmail = it.email,
                        findNickName = it.nickname,
                        findProfileUrl = it.profileUrl,
                        isShowInfoPage = true
                    )
                }
            }
        )
    }

    fun onConfirmButtonClick() {
        if (uiState.value.isFindByEmail) {
            // TODO ??
        } else {
            findIdByNickName()
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