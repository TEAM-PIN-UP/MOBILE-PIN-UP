package com.pinup.pinup.ui.setting.unregister

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.usecase.UnRegisterUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch


class UnRegisterViewModel (
    private val unRegisterUseCase: UnRegisterUseCase
) : BaseViewModel<UnRegisterUiState, UnRegisterUiEvent>(UnRegisterUiState()) {

    fun unregister() = viewModelScope.launch {
        resultResponse(
            response = unRegisterUseCase(),
            successCallback = {
                emitEvent(UnRegisterUiEvent.MoveOnBoarding)
            }
        )
    }

    fun onClickCheck() {
        updateState {
            copy(
                isCheck = !isCheck
            )
        }
    }
}

data class UnRegisterUiState(
    val isCheck: Boolean = false
) : UiState

sealed interface UnRegisterUiEvent : UiEvent {
    data object MoveOnBoarding : UnRegisterUiEvent
}
