package com.pinup.placePinup.ui.onboarding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingRoute(
    onMoveSignUpOnboarding: () -> Unit,
    onMoveLogin: () -> Unit,
    onMoveMain: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                OnboardingUiEvent.MoveSignUpOnboarding -> onMoveSignUpOnboarding()
                OnboardingUiEvent.MoveLogin -> onMoveLogin()
                OnboardingUiEvent.MoveMain -> onMoveMain()
            }
        }
    }

    OnboardingScreen()
}