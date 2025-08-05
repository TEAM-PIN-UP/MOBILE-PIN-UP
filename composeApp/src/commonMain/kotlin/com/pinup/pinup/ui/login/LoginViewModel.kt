package com.pinup.pinup.ui.login

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.usecase.EmailLoginUseCase
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

class LoginViewModel (
    private val contextFactory: ContextFactory,
    private val loginUseCase: EmailLoginUseCase,
    private val socialLoginUseCase: SocialLoginUseCase,
    private val snsLoginFactory: SNSLoginFactory
) : BaseViewModel<LoginUiState, LoginUiEvent>(LoginUiState()) {
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
        snsLoginFactory.doLogin(snsType, contextFactory, loginResultListener)
    }

    private fun login(snsLoginInfo: SNSUserInfo) {
        viewModelScope.launch {
            resultResponse(loginUseCase(snsLoginInfo), { emitEvent( LoginUiEvent.MoveMain ) }, { handleFailLogin(it, snsLoginInfo)} )
        }
    }

    private fun handleFailLogin(code : String, snsLoginInfo : SNSUserInfo) {
//        if (code == StatusCode.Login.NOT_EXIST_MEMBER) {
//            emitEvent(LoginUiEvent.MoveSignUp(snsLoginInfo))
//        }
    }

    fun onIdChange(id: String) {
        updateState { copy(id = id) }
    }

    fun onPasswordChange(password: String) {
        updateState { copy(password = password) }
    }

    fun onClickLogin(){
        //TODO: 로그인 로직 구현
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
}