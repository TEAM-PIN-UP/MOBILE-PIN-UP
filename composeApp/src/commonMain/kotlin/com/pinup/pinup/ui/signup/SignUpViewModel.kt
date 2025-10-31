package com.pinup.pinup.ui.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.data.request.EmailVerifyRequest
import com.pinup.pinup.data.request.SendVerifyCodeRequest
import com.pinup.pinup.domain.model.ImageUploadType
import com.pinup.pinup.domain.model.SignUpInfo
import com.pinup.pinup.domain.usecase.CheckNickNameUseCase
import com.pinup.pinup.domain.usecase.PostEmailVerifyUseCase
import com.pinup.pinup.domain.usecase.PostSendVerifyCodeUseCase
import com.pinup.pinup.domain.usecase.EmailSignUpUseCase
import com.pinup.pinup.domain.usecase.PostImageUploadUseCase
import com.pinup.pinup.domain.usecase.SocialSignUpUseCase
import com.pinup.pinup.domain.validator.NickNameValidator
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.util.Const
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)

class SignUpViewModel (
    savedStateHandle: SavedStateHandle,
    private val checkNickNameUseCase: CheckNickNameUseCase,
    private val sendVerifyCodeUseCase: PostSendVerifyCodeUseCase,
    private val emailVerifyUseCase: PostEmailVerifyUseCase,
    private val socialSignUpUseCase: SocialSignUpUseCase,
    private val emailSignUpUseCase: EmailSignUpUseCase,
    private val uploadImageUploadUseCase: PostImageUploadUseCase
) : BaseViewModel<SignUpUiState, SignUpUiEvent>(SignUpUiState()) {

    private val snsUserInfo = Json.decodeFromString<SNSUserInfo>(savedStateHandle.get<String>(SNS_USER_INFO) ?: "")
    private val query = MutableStateFlow("")
    private var timer = INIT_TIME

    init {
        updateState {
            copy(
                nicknameState = NickNameState(
                    nickname = ""
                ),
                snsType = snsUserInfo.snsType,
                socialId = snsUserInfo.socialId,
                name = snsUserInfo.name ?: snsUserInfo.nickname ?: ""
            )
        }

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
        updateState {
            copy(
                emailState = uiState.value.emailState.copy(
                    verificationCode = code,
                )
            )
        }

        if (code.isEmpty()) {
            updateState {
                copy(
                    emailState = uiState.value.emailState.copy(
                        emailVerifyType = EmailVerifyType.NONE,
                    )
                )
            }
        }

        if (code.length == 6) {
           verifyEmail()
        }
    }

    private fun verifyEmail() = viewModelScope.launch {
        val request = EmailVerifyRequest(
            email = uiState.value.emailState.email,
            code = uiState.value.emailState.verificationCode
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
                emailState = emailState.copy(
                    emailVerifyType = type
                )
            )
        }
    }

    fun onClickVerify() = viewModelScope.launch {
        if (isValidEmail()) {
            val request = SendVerifyCodeRequest(
                email = uiState.value.emailState.email
            )
            resultResponse(
                response = sendVerifyCodeUseCase(request),
                successCallback = {
                    startTimer()
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
                    isClickedVerify = isValid,
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
                passwordState = passwordState.copy(
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


    private fun checkNickName(nickname: String) = viewModelScope.launch {
        handleSuccessCheckNickName(false)
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
        if(uiState.value.profileUrl.isEmpty()) {
            if (uiState.value.snsType == SNSType.PINUP) {
                emailSignUp("")
            } else {
                socialSignUp("")
            }
        } else {
            resultResponse(
                response = uploadImageUploadUseCase(ImageUploadType.PROFILES, uiState.value.profileUrl),
                successCallback = {
                    if (uiState.value.snsType == SNSType.PINUP) {
                        emailSignUp(it)
                    } else {
                        socialSignUp(it)
                    }
                }
            )
        }
    }

    private fun socialSignUp(profile: String) = viewModelScope.launch {
        val request = SignUpInfo(
            email = uiState.value.emailState.email,
            socialId = uiState.value.socialId,
            nickname = uiState.value.nicknameState.nickname,
            name = uiState.value.nicknameState.nickname,
            loginType = uiState.value.snsType,
            termsOfMarketing = uiState.value.termsOfServiceState.isMarketingAgreeAgree,
            profileImageUrl = profile
        )
        resultResponse(
            response = socialSignUpUseCase(request),
            successCallback = { emitEvent(SignUpUiEvent.MoveMain) }
        )
    }

    private fun emailSignUp(profile: String) = viewModelScope.launch {
        val request = SignUpInfo(
            email = uiState.value.emailState.email,
            nickname = uiState.value.nicknameState.nickname,
            password = uiState.value.passwordState.password,
            name = uiState.value.nicknameState.nickname,
            loginType = uiState.value.snsType,
            termsOfMarketing = uiState.value.termsOfServiceState.isMarketingAgreeAgree,
            profileImageUrl = profile
        )
        resultResponse(
            response = emailSignUpUseCase(request),
            successCallback = { emitEvent(SignUpUiEvent.MoveMain) }
        )
    }

    private fun startTimer() {
        timer = INIT_TIME
        viewModelScope.launch {
            while (timer >= 0) {
                val m = timer / 60
                val s = timer % 60
                val textSecond = if(s < 10) "0$s" else "$s"
                updateState {
                    copy(
                        emailState = emailState.copy(
                            timer = "$m:$textSecond"
                        )
                    )
                }
                timer -= 1
                delay(1.seconds)
            }
        }
    }

    companion object {
        private const val SNS_USER_INFO = "snsUserInfo"
        private const val INIT_TIME = 300
    }
}

sealed interface SignUpUiEvent : UiEvent{
    data object MoveMain : SignUpUiEvent
}

data class SignUpUiState(
    val snsType: SNSType = SNSType.KAKAO,
    val emailState: EmailState = EmailState(),
    val passwordState: PasswordState = PasswordState(),
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
    val timer: String = "",
    val isEmailValid: Boolean = true,
    val isEmailUsed: Boolean = false,
    val emailVerifyType: EmailVerifyType = EmailVerifyType.NONE,
    val isClickedVerify : Boolean = false,
) {
    val isPassValidation = isEmailValid && !isEmailUsed && email.isNotEmpty() && emailVerifyType == EmailVerifyType.VERIFIED
}

data class PasswordState(
    val password: String = "",
    val passwordAgain : String = "",
    val isPasswordValid: Boolean = true,
    val isPasswordMatched: Boolean = true,
    val isShowPassword: Boolean = false,
) {
    val isPassValidation = isPasswordValid && isPasswordMatched && password.isNotEmpty() && passwordAgain.isNotEmpty()
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
    val isNicknameUsed: Boolean = false,
) {
    val isPassNickname: Boolean = nickname.isNotEmpty() && !isNicknameUsed
}