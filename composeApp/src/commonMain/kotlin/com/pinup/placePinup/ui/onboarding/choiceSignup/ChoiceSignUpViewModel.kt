
package com.pinup.placePinup.ui.onboarding.choiceSignup

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.StatusCode
import com.pinup.placePinup.domain.model.isSuccess
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

class ChoiceSignUpViewModel (
    private val contextFactory: ContextFactory,
    private val socialLoginUseCase: SocialLoginUseCase,
    private val snsLoginFactory: SNSLoginFactory,
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
) : BaseViewModel<UiState, ChoiceSignUpUiEvent>(UiState.Default) {
    private val loginResultListener = object : SNSLoginResultListener {
        override fun onCancel() {
            hLog("login cancel")
        }

        override fun onFail(message: String?) {
            hLog(message ?: "error")
        }

        override fun onSuccess(snsLoginInfo: SNSUserInfo) {
            login(snsLoginInfo)
        }

    }
    fun doSNSLogin(snsType: SNSType) {
        // 요청 중 연타로 로그인이 중복으로 나가지 않게 막는다.
        if (isLoading.value) return

        if(snsType == SNSType.PINUP) {
            emitEvent(ChoiceSignUpUiEvent.MoveEmailLogin)
        }
        else {
            snsLoginFactory.doLogin(snsType, contextFactory, loginResultListener)
        }
    }

    // SNS SDK 화면이 끝나고 우리 서버와 통신할 때부터 로딩을 띄운다.
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
                        handleLoginError(it, snsLoginInfo)
                    }
                )
                // 기기 토큰 등록까지 같은 로딩 안에서 끝낸 뒤 이동한다. 실패해도 이동은 막지 않는다.
                if (result.isSuccess()) {
                    registerDeviceTokenUseCase()
                    emitEvent(ChoiceSignUpUiEvent.MoveMain)
                }
            }
        }
    }

    private fun handleLoginError(code : String, snsLoginInfo : SNSUserInfo){
        when(code) {
            StatusCode.Login.NOT_EXIST_MEMBER -> emitEvent(ChoiceSignUpUiEvent.MoveSignUp(snsLoginInfo))
            else -> hLog(code) //TODO 나중에 TOAST같은 오류처리
        }
    }


}

sealed interface ChoiceSignUpUiEvent : UiEvent {
    data class MoveSignUp(val snsLoginInfo: SNSUserInfo) : ChoiceSignUpUiEvent
    data object MoveMain : ChoiceSignUpUiEvent
    data object MoveEmailLogin : ChoiceSignUpUiEvent
}