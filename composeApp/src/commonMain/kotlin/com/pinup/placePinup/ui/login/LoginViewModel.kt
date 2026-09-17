package com.pinup.placePinup.ui.login

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.data.request.EmailLoginRequest
import com.pinup.placePinup.domain.model.StatusCode
import com.pinup.placePinup.domain.model.isSuccess
import com.pinup.placePinup.domain.usecase.EmailLoginUseCase
import com.pinup.placePinup.domain.usecase.RegisterDeviceTokenUseCase
import com.pinup.placePinup.domain.usecase.SocialLoginUseCase
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.platform.hLog
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.login.model.SNSUserInfo
import com.pinup.placePinup.ui.login.sns.SNSLoginFactory
import com.pinup.placePinup.ui.login.sns.SNSLoginResultListener
import kotlinx.coroutines.launch

class LoginViewModel (
    private val contextFactory: ContextFactory,
    private val emailLoginUseCase: EmailLoginUseCase,
    private val socialLoginUseCase: SocialLoginUseCase,
    private val snsLoginFactory: SNSLoginFactory,
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
) : BaseViewModel<LoginUiState, LoginUiEvent>(LoginUiState()) {
    private val loginResultListener = object : SNSLoginResultListener {
        override fun onCancel() {
            emitEvent(LoginUiEvent.TestError("cancel"))
        }

        override fun onFail(message: String?) {
            emitEvent(LoginUiEvent.TestError(message ?: "error"))
        }

        override fun onSuccess(snsLoginInfo: SNSUserInfo) {
            login(snsLoginInfo)
        }

    }
    fun doSNSLogin(snsType: SNSType) {
        // 요청 중 연타로 로그인이 중복으로 나가지 않게 막는다.
        if (isLoading.value) return

        if (snsType == SNSType.PINUP) {
            emailLogin()
            return
        }

        snsLoginFactory.doLogin(snsType, contextFactory, loginResultListener)
    }

    // SNS SDK 화면이 끝나고 우리 서버와 통신할 때부터 로딩을 띄운다.
    // (SDK 인증 화면 위에 띄우면 취소 콜백이 안 오는 경우 다이얼로그가 남을 수 있다)
    private fun login(snsLoginInfo: SNSUserInfo) {
        viewModelScope.launch {
            // SDK 콜백이 백그라운드 스레드에서 연달아 올 수 있어 Main 에서 한 번 더 막는다.
            if (isLoading.value) return@launch
            withLoading {
                val result = socialLoginUseCase(snsLoginInfo)
                resultResponse(
                    response = result,
                    successCallback = {},
                    errorCallback = {
                        handleFailSocialLogin(it, snsLoginInfo)
                    }
                )
                if (result.isSuccess()) registerDeviceTokenAndMoveMain()
            }
        }
    }

    private fun emailLogin() {
        val request = EmailLoginRequest(
            email = uiState.value.id,
            password = uiState.value.password
        )
        viewModelScope.launch {
            withLoading {
                val result = emailLoginUseCase(request)
                resultResponse(
                    response = result,
                    successCallback = {},
                    errorCallback = ::handleFailEmailLogin
                )
                if (result.isSuccess()) registerDeviceTokenAndMoveMain()
            }
        }
    }

    private fun handleFailSocialLogin(code : String, snsLoginInfo : SNSUserInfo) {
        if (code == StatusCode.Login.NOT_EXIST_MEMBER) {
            emitEvent(LoginUiEvent.MoveSignUp(snsLoginInfo))
        }
    }

    private fun handleFailEmailLogin(code : String){
        if (code == StatusCode.Login.NOT_EXIST_MEMBER) {
            updateState {
                copy(
                    isError = true
                )
            }
        }
    }

    fun onIdChange(id: String) {
        updateState {
            copy(
                id = id,
                isError = false
            )
        }
    }

    fun onPasswordChange(password: String) {
        updateState {
            copy(
                password = password,
                isError = false
            )
        }
    }

    // 기기 토큰 등록까지 같은 로딩 안에서 끝낸 뒤 메인으로 이동한다.
    // 등록 실패는 푸시만 못 받을 뿐이라 이동을 막지 않는다. (다음 자동 로그인에서 재등록)
    private suspend fun registerDeviceTokenAndMoveMain() {
        registerDeviceTokenUseCase()
        emitEvent(LoginUiEvent.MoveMain)
    }
}

data class LoginUiState(
    val id: String = "",
    val password: String = "",
    val isError : Boolean = false,
) : UiState

sealed interface LoginUiEvent : UiEvent {
    data class MoveSignUp(val snsLoginInfo: SNSUserInfo) : LoginUiEvent
    data object MoveMain : LoginUiEvent
    data class TestError(val test: String) : LoginUiEvent
}