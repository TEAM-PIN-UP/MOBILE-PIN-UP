package com.pinup.pinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.usecase.IsLoginUseCase
import com.pinup.pinup.domain.usecase.LogoutUseCase
import com.pinup.pinup.event.LogoutEventBus
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartAppViewModel(
    private val isLoginUseCase: IsLoginUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StartAppUiState())
    val uiState: StateFlow<StartAppUiState>
        get() = _uiState.asStateFlow()

    init {
        getIsLogin()
        initLogoutEventBus()
    }

    private fun getIsLogin() = viewModelScope.launch {
        _uiState.update {
            it.copy(
                isLogin = false
            )
        }
    }

    @OptIn(FlowPreview::class)
    private fun initLogoutEventBus() = viewModelScope.launch {
        LogoutEventBus.logoutEvent
            .debounce(300)
            .collectLatest { message ->
                logoutUseCase()
                _uiState.update {
                    it.copy(
                        alertState = it.alertState.copy(
                            title = message ?: "",
                            isShow = true
                        )
                    )
                }
            }
    }

    fun dismissAlert() {
        _uiState.update {
            it.copy(
                alertState = AlertState()
            )
        }
    }
}

data class StartAppUiState(
    val isLogin: Boolean? = null,
    val alertState: AlertState = AlertState()
)

data class AlertState(
    val title: String = "",
    val message: String = "",
    val isShow: Boolean = false,
    val leftButtonText: String = "",
    val rightButtonText: String = "",
    val onLeftButtonClick: () -> Unit = {},
    val onRightButtonClick: () -> Unit = {},
)