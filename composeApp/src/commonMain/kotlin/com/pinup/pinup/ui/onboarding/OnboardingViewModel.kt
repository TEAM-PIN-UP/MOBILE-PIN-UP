package com.pinup.pinup.ui.onboarding

import androidx.lifecycle.viewModelScope
import com.pinup.pinup.domain.usecase.IsLoginUseCase
import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState
import kotlinx.coroutines.launch

class OnboardingViewModel (
    private val isLoginUseCase: IsLoginUseCase,
) : BaseViewModel<UiState, OnboardingUiEvent>(UiState.Default) {

    fun onClickStart() = viewModelScope.launch {
        val isLogin = isLoginUseCase()
        if(isLogin) emitEvent(OnboardingUiEvent.MoveLogin)
        else emitEvent(OnboardingUiEvent.MoveSignUpOnboarding)
    }
}

sealed interface OnboardingUiEvent : UiEvent{
    data object MoveSignUpOnboarding : OnboardingUiEvent
    data object MoveLogin : OnboardingUiEvent
}