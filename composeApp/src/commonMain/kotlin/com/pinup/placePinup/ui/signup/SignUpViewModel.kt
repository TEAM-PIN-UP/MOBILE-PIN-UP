package com.pinup.placePinup.ui.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.EmailVerifyRequest
import com.pinup.placePinup.data.request.SendVerifyCodeRequest
import com.pinup.placePinup.domain.model.ImageUploadType
import com.pinup.placePinup.domain.model.SignUpInfo
import com.pinup.placePinup.domain.model.getSuccessOrNull
import com.pinup.placePinup.domain.usecase.CheckNickNameUseCase
import com.pinup.placePinup.domain.usecase.PostEmailVerifyUseCase
import com.pinup.placePinup.domain.usecase.PostSendVerifyCodeUseCase
import com.pinup.placePinup.domain.usecase.EmailSignUpUseCase
import com.pinup.placePinup.domain.usecase.PostImageUploadUseCase
import com.pinup.placePinup.domain.usecase.SocialSignUpUseCase
import com.pinup.placePinup.domain.validator.NickNameValidator
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.login.model.SNSUserInfo
import com.pinup.placePinup.util.Const
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
        // 인증 메일 발송은 응답이 느려 연타하면 메일이 여러 통 나가므로 요청 중엔 막는다.
        if (isLoading.value) return@launch
        if (isValidEmail()) {
            val request = SendVerifyCodeRequest(
                email = uiState.value.emailState.email
            )
            withLoading {
                resultResponse(
                    response = sendVerifyCodeUseCase(request),
                    successCallback = {
                        updateState {
                            copy(
                                emailState = emailState.copy(
                                    isClickedVerify = true
                                )
                            )
                        }
                        startTimer()
                    }
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
                passwordState = passwordState.copy(
                    passwordAgain = password,
                    isPasswordMatched = password == passwordState.password
                )
            )
        }
    }

    fun onClickShowFirstPassword() = viewModelScope.launch {
        updateState {
            copy(
                passwordState = uiState.value.passwordState.copy(
                    isShowFirstPassword = !uiState.value.passwordState.isShowFirstPassword,
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
        // 가입 요청이 중복으로 나가면 두 번째 요청이 '이미 가입된 회원' 오류가 되므로 요청 중엔 막는다.
        if (isLoading.value) return@launch
        // 프로필 업로드와 가입 요청을 순서대로 한 로딩 안에서 처리한다.
        withLoading {
            val profile = if (uiState.value.profileUrl.isEmpty()) {
                null
            } else {
                val upload = uploadImageUploadUseCase(ImageUploadType.PROFILES, uiState.value.profileUrl)
                resultResponse(
                    response = upload,
                    successCallback = {}
                )
                upload.getSuccessOrNull() ?: return@withLoading
            }

            if (uiState.value.snsType == SNSType.PINUP) {
                emailSignUp(profile)
            } else {
                socialSignUp(profile)
            }
        }
    }

    private suspend fun socialSignUp(profile: String?) {
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

    private suspend fun emailSignUp(profile: String?) {
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
    val isShowFirstPassword: Boolean = false,
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