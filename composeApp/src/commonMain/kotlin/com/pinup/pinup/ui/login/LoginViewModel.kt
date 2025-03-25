package com.pinup.pinup.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.usecase.LoginUseCase
import com.pinup.pinup.hLog
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.login.sns.SNSLoginResultFactory
import com.pinup.pinup.ui.login.sns.SNSLoginResultListener
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch


class LoginViewModel (
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
    val uiEvent: SharedFlow<LoginUiEvent>
        get() = _uiEvent.asSharedFlow()
    private val loginResultListener = object : SNSLoginResultListener {
        override fun onCancel() {
            TODO("Not yet implemented")
        }

        override fun onFail(message: String?) {
            TODO("Not yet implemented")
        }

        override fun onSuccess(snsLoginInfo: SNSUserInfo) {
            login(snsLoginInfo)
        }

    }
    fun doSNSLogin(snsType: SNSType) {
        val snsLoginController = SNSLoginResultFactory.initialize(snsType)
        snsLoginController.doLogin(
            context = context,
            resultListener = loginResultListener
        )
    }

    private fun login(snsLoginInfo: SNSUserInfo) = viewModelScope.launch {
        when (val result = loginUseCase.invoke(snsLoginInfo)) {
            is PResult.Fail -> {
                hLog("fail >> ${result.failState}")
                if (result.failState.code == "M001") {
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