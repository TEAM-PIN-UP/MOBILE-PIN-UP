package com.pinup.pinup.ui.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.SignUpInfo
import com.pinup.pinup.domain.usecase.CheckNickNameUseCase
import com.pinup.pinup.domain.usecase.SignUpUseCase
import com.pinup.pinup.domain.validator.NickNameValidator
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(FlowPreview::class)

class SignUpViewModel (
    savedStateHandle: SavedStateHandle,
    private val checkNickNameUseCase: CheckNickNameUseCase,
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpUiState, SignUpUiEvent>(SignUpUiState()) {
    private val snsUserInfo = Json.decodeFromString<SNSUserInfo>(savedStateHandle.get<String>(SNS_USER_INFO) ?: "")
    private val query = MutableStateFlow("")

    init {
        updateState { copy(
            nicknameState = NickNameState(
                nickname = ""
            ),
            snsType = snsUserInfo.snsType,
            socialId = snsUserInfo.socialId,
            name = snsUserInfo.name ?: snsUserInfo.nickname ?: ""
        ) }

        viewModelScope.launch {
            query
                .filter {
                    it.isNotBlank()
                }
                .debounce(300)
                .collectLatest {
                    checkNickName(it)
                }
        }
        updateNickName(uiState.value.nicknameState.nickname)
    }

    fun updateProfile(profileUrl: ByteArray) {
        updateState { copy( profileUrl = profileUrl ) }
    }

    fun updateNickName(nickname: String) = viewModelScope.launch {
        if (NickNameValidator.checkNameValidation(nickname).not()) return@launch
        query.emit(nickname)
        updateState { copy(
            nicknameState = uiState.value.nicknameState.copy(
                nickname = nickname
            )
        ) }
    }

    fun updateEmail(email: String) = viewModelScope.launch {
        updateState { copy(
            emailState = uiState.value.emailState.copy(
                email = email
            )
        ) }
    }

    fun updateVerificationCode(code: String) = viewModelScope.launch {
        updateState { copy(
            emailState = uiState.value.emailState.copy(
                verificationCode = code
            )
        ) }
    }

    fun onClickVerify() = viewModelScope.launch {
        //TODO: 이메일 인증 로직 구현
        updateState { copy(
            emailState = emailState.copy(
                isClickedVerify = true,
            )
        ) }
    }

    private fun checkNickName(nickname: String) = viewModelScope.launch {
        resultResponse(checkNickNameUseCase(nickname), ::handleSuccessCheckNickName)
    }

    private fun handleSuccessCheckNickName(isNicknameUsed: Boolean) {
        updateState { copy(
            nicknameState = uiState.value.nicknameState.copy(
                isNicknameUsed = isNicknameUsed
            )
        ) }
    }

    fun updateTermsOfServiceState(termsOfService: TermsOfService) = viewModelScope.launch {
        var termsOfServiceState = uiState.value.termsOfServiceState
        when(termsOfService) {
            TermsOfService.ALL -> {
                termsOfServiceState = termsOfServiceState.copy(
                    isUsingServiceAgree = termsOfServiceState.isAllAgree.not(),
                    isMarketingAgreeAgree = termsOfServiceState.isAllAgree.not(),
                    isCollectDataAgree = termsOfServiceState.isAllAgree.not(),
                    isCollectLocationAgree = termsOfServiceState.isAllAgree.not(),
                )
            }
            TermsOfService.USING_SERVICE -> {
                termsOfServiceState = termsOfServiceState.copy(
                    isUsingServiceAgree = termsOfServiceState.isUsingServiceAgree.not()
                )
            }
            TermsOfService.COLLECT_DATA -> {
                termsOfServiceState = termsOfServiceState.copy(
                    isCollectDataAgree = termsOfServiceState.isCollectDataAgree.not()
                )
            }
            TermsOfService.COLLECT_LOCATION -> {
                termsOfServiceState = termsOfServiceState.copy(
                    isCollectLocationAgree = termsOfServiceState.isCollectLocationAgree.not()
                )
            }
            TermsOfService.MARKETING -> {
                termsOfServiceState = termsOfServiceState.copy(
                    isMarketingAgreeAgree = termsOfServiceState.isMarketingAgreeAgree.not()
                )
            }
        }
        updateState { copy( termsOfServiceState = termsOfServiceState) }
    }

    fun signUp() = viewModelScope.launch {
        val request = SignUpInfo(
            email = uiState.value.emailState.email,
            socialId = uiState.value.socialId,
            nickname = uiState.value.nicknameState.nickname,
            name = uiState.value.name,
            loginType = uiState.value.snsType,
            termsOfMarketing = uiState.value.termsOfServiceState.isMarketingAgreeAgree,
            profileImage = uiState.value.profileUrl
        )
        resultResponse(signUpUseCase(request), { emitEvent(SignUpUiEvent.MoveMain) })
    }

    companion object {
        private const val SNS_USER_INFO = "snsUserInfo"
    }
}

sealed interface SignUpUiEvent : UiEvent{
    data object MoveMain : SignUpUiEvent
}

data class SignUpUiState(
    val snsType: SNSType = SNSType.KAKAO,
    val emailState: EmailState = EmailState(),
    val nicknameState: NickNameState = NickNameState(),
    val socialId: String = "",
    val profileUrl: ByteArray = ByteArray(0),
    val name: String = "",
    val termsOfServiceState: TermsOfServiceState = TermsOfServiceState(),
) : UiState

enum class TermsOfService {
    ALL,
    USING_SERVICE,
    COLLECT_DATA,
    COLLECT_LOCATION,
    MARKETING
}

enum class EmailVerifyType {
    NONE, VERIFIED, NOT_VERIFIED
}

data class EmailState(
    val email: String = "",
    val verificationCode: String = "",
    val isEmailValid: Boolean = false,
    val isEmailUsed: Boolean = false,
    val emailVerifyType: EmailVerifyType = EmailVerifyType.NONE,
    val isClickedVerify : Boolean = false,
) {
    val isPassValidation = !isEmailValid && !isEmailUsed && email.isNotEmpty() && emailVerifyType == EmailVerifyType.VERIFIED
}

data class TermsOfServiceState(
    val isUsingServiceAgree: Boolean = false,
    val isCollectDataAgree: Boolean = false,
    val isCollectLocationAgree: Boolean = false,
    val isMarketingAgreeAgree: Boolean = false
) {
    val isAllAgree = isUsingServiceAgree && isCollectDataAgree && isCollectLocationAgree
            && isMarketingAgreeAgree
    val isPassValidation = isUsingServiceAgree && isCollectDataAgree && isCollectLocationAgree
}

data class NickNameState(
    val nickname: String = "",
    val isNicknameUsed: Boolean? = null,
)