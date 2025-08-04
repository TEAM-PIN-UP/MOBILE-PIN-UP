package com.pinup.pinup.ui.onboarding

import com.pinup.pinup.ui.base.BaseViewModel
import com.pinup.pinup.ui.base.UiEvent
import com.pinup.pinup.ui.base.UiState

class OnboardingViewModel (
) : BaseViewModel<UiState, OnboardingUiEvent>(UiState.Default) {

}

sealed interface OnboardingUiEvent : UiEvent{
    data object MoveSignUpOnboarding : OnboardingUiEvent
    data object MoveLogin : OnboardingUiEvent
}