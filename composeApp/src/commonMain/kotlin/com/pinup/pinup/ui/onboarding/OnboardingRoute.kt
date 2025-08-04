package com.pinup.pinup.ui.onboarding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.pinup.pinup.platform.ContextFactory
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun OnboardingRoute(
    contextFactory: ContextFactory,
    onMoveSignUpOnboarding: () -> Unit,
    onMoveLogin: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel(parameters = { parametersOf(contextFactory) })
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
        onClickStart = onMoveSignUpOnboarding
    )
}