
package com.pinup.placePinup.ui.onboarding.choiceSignup

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.StatusCode
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
    private val snsLoginFactory: SNSLoginFactory
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
        if(snsType == SNSType.PINUP) {
            emitEvent(ChoiceSignUpUiEvent.MoveSignUp(
                SNSUserInfo(snsType = snsType)
            ))
        }
        else {
            snsLoginFactory.doLogin(snsType, contextFactory, loginResultListener)
        }
    }

    private fun login(snsLoginInfo: SNSUserInfo) {
        viewModelScope.launch {
            resultResponse(
                response = socialLoginUseCase(snsLoginInfo),
                successCallback = {
                    emitEvent( ChoiceSignUpUiEvent.MoveMain )
                },
                errorCallback = {
                    handleLoginError(it, snsLoginInfo)
                }
            )
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