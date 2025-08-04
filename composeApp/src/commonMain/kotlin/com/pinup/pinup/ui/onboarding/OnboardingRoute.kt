package com.pinup.pinup.ui.onboarding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun OnboardingRoute(
    onMoveSignUpOnboarding: () -> Unit,
    onMoveLogin: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                OnboardingUiEvent.MoveSignUpOnboarding -> onMoveSignUpOnboarding()
                OnboardingUiEvent.MoveLogin -> onMoveLogin
            }
        }
    }
    OnboardingScreen(
        //TODO 임시, 추후 회원가입 여부 로직 추가
        onClickStart = viewModel::onClickStart
    )
}