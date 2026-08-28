package com.pinup.placePinup.ui.onboarding

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.usecase.IsLoginUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val MIN_SPLASH_MS = 500L

class OnboardingViewModel (
    private val isLoginUseCase: IsLoginUseCase,
) : BaseViewModel<UiState, OnboardingUiEvent>(UiState.Default) {

    init {
        onClickStart()
    }

    private fun onClickStart() = viewModelScope.launch {
        // 로그인 판정을 스플래시 표시와 병렬로 시작하고, 브랜드 노출은 최소 500ms만 보장한다.
        // (기존: 2초를 통째로 기다린 뒤에야 판정을 시작 → 콜드 스타트 체감 지연의 주범)
        val decision = async { isLoginUseCase() }
        delay(MIN_SPLASH_MS)
        val (isMember, isLogin) = decision.await()
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