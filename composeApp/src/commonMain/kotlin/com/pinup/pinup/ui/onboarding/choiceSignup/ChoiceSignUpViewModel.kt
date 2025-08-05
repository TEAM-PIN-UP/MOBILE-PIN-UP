
package com.pinup.pinup.ui.onboarding.choiceSignup

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.StatusCode
import com.pinup.pinup.domain.usecase.SocialLoginUseCase
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.login.sns.SNSLoginFactory
import com.pinup.pinup.ui.login.sns.SNSLoginResultListener
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
        if(snsType == SNSType.EMAIL) {
//            emitEvent(ChoiceSignUpUiEvent.MoveEmailLogin)
//            return
            //TODO 임시용, 제거 예정
            emitEvent(ChoiceSignUpUiEvent.MoveSignUp(
                SNSUserInfo(
                    socialId = "",
                    snsType = snsType,
                    email = "",
                    name = "",
                    nickname = "",
                )
            ))
        }
        snsLoginFactory.doLogin(snsType, contextFactory, loginResultListener)
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