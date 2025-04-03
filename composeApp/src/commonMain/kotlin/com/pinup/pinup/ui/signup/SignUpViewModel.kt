package com.pinup.pinup.ui.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SignUpInfo
import com.pinup.pinup.domain.usecase.CheckNickNameUseCase
import com.pinup.pinup.domain.usecase.LoginUseCase
import com.pinup.pinup.domain.usecase.SignUpUseCase
import com.pinup.pinup.domain.validator.NickNameValidator
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@OptIn(FlowPreview::class)

class SignUpViewModel (
    savedStateHandle: SavedStateHandle,
    private val checkNickNameUseCase: CheckNickNameUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val snsUserInfo = Json.decodeFromString<SNSUserInfo>(savedStateHandle.get<String>(SNS_USER_INFO) ?: "")
    private val _uiState = MutableStateFlow(SignUpUiState(
        nicknameState = NickNameState(
            nickname = snsUserInfo.nickname ?: ""
        ),
        snsType = snsUserInfo.snsType,
        socialId = snsUserInfo.socialId,
        email = snsUserInfo.email ?: "",
        name = snsUserInfo.name ?: snsUserInfo.nickname ?: ""
    ))
    val uiState: StateFlow<SignUpUiState>
        get() = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SignUpUiEvent>()
    val uiEvent: SharedFlow<SignUpUiEvent>
        get() = _uiEvent.asSharedFlow()

    private val query = MutableStateFlow("")

    init {
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
        updateNickName(_uiState.value.nicknameState.nickname)
    }

    fun updateProfile(profileUrl: ByteArray) {
        _uiState.update {
            it.copy(
                profileUrl = profileUrl
            )
        }
    }

    fun updateNickName(nickname: String) = viewModelScope.launch {
        if (NickNameValidator.checkNameValidation(nickname).not()) return@launch
        query.emit(nickname)
        _uiState.update {
            it.copy(
                nicknameState = it.nicknameState.copy(
                    nickname = nickname
                )
            )
        }
    }

    private fun checkNickName(nickname: String) = viewModelScope.launch {
        when (val result = checkNickNameUseCase.invoke(nickname)) {
            is PResult.Fail -> {

            }
            is PResult.Success -> {
                _uiState.update {
                    it.copy(
                        nicknameState = it.nicknameState.copy(
                            isNicknameUsed = result.data
                        )
                    )
                }
            }
        }
    }

    fun updateTermsOfServiceState(termsOfService: TermsOfService) = viewModelScope.launch {
        var termsOfServiceState = _uiState.value.termsOfServiceState
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
        _uiState.update {
            it.copy(
                termsOfServiceState = termsOfServiceState
            )
        }
    }

    fun signUp() = viewModelScope.launch {
        val request = SignUpInfo(
            email = _uiState.value.email,
            socialId = _uiState.value.socialId,
            nickname = _uiState.value.nicknameState.nickname,
            name = _uiState.value.name,
            loginType = _uiState.value.snsType,
            termsOfMarketing = _uiState.value.termsOfServiceState.isMarketingAgreeAgree,
            profileImage = _uiState.value.profileUrl
        )
        when (val result = signUpUseCase(request)) {
            is PResult.Fail -> {
                hLog("result >> ${result.failState}")
            }
            is PResult.Success -> {
                login()
            }
        }
    }

    private fun login() = viewModelScope.launch {
        when (val result = loginUseCase(snsUserInfo)) {
            is PResult.Fail -> {
                hLog("result >> ${result.failState}")
            }
            is PResult.Success -> {
                _uiEvent.emit(SignUpUiEvent.MoveMain)
            }
        }
    }

    companion object {
        private const val SNS_USER_INFO = "snsUserInfo"
    }
}

sealed interface SignUpUiEvent {
    data object MoveMain : SignUpUiEvent
}

data class SignUpUiState(
    val snsType: SNSType = SNSType.KAKAO,
    val nicknameState: NickNameState = NickNameState(),
    val socialId: String = "",
    val profileUrl: ByteArray = ByteArray(0),
    val email: String = "",
    val name: String = "",
    val termsOfServiceState: TermsOfServiceState = TermsOfServiceState(),
)

enum class TermsOfService {
    ALL,
    USING_SERVICE,
    COLLECT_DATA,
    COLLECT_LOCATION,
    MARKETING
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