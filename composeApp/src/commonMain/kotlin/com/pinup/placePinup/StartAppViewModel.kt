package com.pinup.placePinup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.UpdateMessage
import com.pinup.placePinup.domain.model.UpdateStore
import com.pinup.placePinup.domain.usecase.LogoutUseCase
import com.pinup.placePinup.event.LogoutEventBus
import com.pinup.placePinup.platform.KakaoDeepLinkStore
import com.pinup.placePinup.util.ScreenState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartAppViewModel(
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StartAppUiState())
    val uiState: StateFlow<StartAppUiState>
        get() = _uiState.asStateFlow()

    init {
        initLogoutEventBus()
        initGetUserId()
        initGetErrorMessage()
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

    private fun initGetUserId() = viewModelScope.launch {
        KakaoDeepLinkStore.params.collect { id ->
            _uiState.update {
                it.copy(
                    userId = id.toInt(),
                )
            }
        }
    }

    private fun initGetErrorMessage() = viewModelScope.launch {
        ScreenState.errorMessage.collect { error ->
            _uiState.update {
                it.copy(
                    errorMessage = error,
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

    fun updateUserId(userId: Int) {
        _uiState.update {
            it.copy(
                userId = userId,
            )
        }
        KakaoDeepLinkStore.onNewParams(userId.toString())
    }

    fun dismissUpdateDialog() {
        _uiState.update {
            it.copy(
                updateDialogState = UpdateDialogState.NoUpdate
            )
        }
    }

    fun remindLaterUpdateDialog() {
        // TODO 다음에 하기 처리

        _uiState.update {
            it.copy(
                updateDialogState = UpdateDialogState.NoUpdate
            )
        }
    }
}

data class StartAppUiState(
    val isLogin: Boolean? = null,
    val alertState: AlertState = AlertState(),
    val updateDialogState: UpdateDialogState = UpdateDialogState.Loading,
    val userId: Int = -1,
    val errorMessage: String = "",
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

sealed interface UpdateDialogState {
    data object Loading : UpdateDialogState
    data object NoUpdate : UpdateDialogState
    data class UpdateRequired(
        val type: UpdateType,
        val message: UpdateMessage,
        val store: UpdateStore
    ) : UpdateDialogState
}

enum class UpdateType {
    OPTIONAL, FORCE
}

// TODO API 연동 후 삭제 예정
val dummyForceUpdateState = UpdateDialogState.UpdateRequired(
    type = UpdateType.FORCE,
    message = UpdateMessage(
        title = "",
        body = ""
    ),
    store = UpdateStore(
        url = "https://play.google.com/store/apps/details?id=com.pinup.placePinup",
        market = "playstore",
        packageNameOrBundleId = "com.pinup.placePinup"
    )
)

val dummyOptionalUpdateState = UpdateDialogState.UpdateRequired(
    type = UpdateType.OPTIONAL,
    message = UpdateMessage(
        title = "",
        body = ""
    ),
    store = UpdateStore(
        url = "https://play.google.com/store/apps/details?id=com.pinup.placePinup",
        market = "playstore",
        packageNameOrBundleId = "com.pinup.placePinup"
    )
)
