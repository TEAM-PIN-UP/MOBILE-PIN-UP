package com.pinup.pinup.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.usecase.SocialLoginUseCase
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.platform.hLog
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.login.sns.SNSLoginFactory
import com.pinup.pinup.ui.login.sns.SNSLoginResultListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class LoginViewModel (
    private val contextFactory: ContextFactory,
    private val socialLoginUseCase: SocialLoginUseCase,
    private val snsLoginFactory: SNSLoginFactory
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent>
        get() = _uiEvent.asSharedFlow()
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

    private fun login(snsLoginInfo: SNSUserInfo) = viewModelScope.launch {
        when (val result = socialLoginUseCase.invoke(snsLoginInfo)) {
            is PResult.Fail -> {
                hLog("fail >> ${result.failState}")
                if (result.failState.code == "E_MEMBER001") {
                    _uiEvent.emit(LoginUiEvent.MoveSignUp(snsLoginInfo))
                }
            }
            is PResult.Success -> {
                hLog("success")
                _uiEvent.emit(LoginUiEvent.MoveMain)
            }
        }
    }
}

sealed interface LoginUiEvent {
    data class MoveSignUp(val snsLoginInfo: SNSUserInfo) : LoginUiEvent
    data object MoveMain : LoginUiEvent
}