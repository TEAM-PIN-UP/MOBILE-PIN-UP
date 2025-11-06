package com.pinup.placePinup.ui.onboarding

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.usecase.IsLoginUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnboardingViewModel (
    private val isLoginUseCase: IsLoginUseCase,
) : BaseViewModel<UiState, OnboardingUiEvent>(UiState.Default) {

    init {
        onClickStart()
    }

    private fun onClickStart() = viewModelScope.launch {
        delay(2000)
        val (isMember, isLogin) = isLoginUseCase()
        if (isMember) {
            emitEvent(if(isLogin) OnboardingUiEvent.MoveMain else OnboardingUiEvent.MoveLogin)
        } else {
            emitEvent(OnboardingUiEvent.MoveSignUpOnboarding)
        }
    }
}

sealed interface OnboardingUiEvent : UiEvent{
    data object MoveSignUpOnboarding : OnboardingUiEvent
    data object MoveLogin : OnboardingUiEvent
    data object MoveMain : OnboardingUiEvent
}