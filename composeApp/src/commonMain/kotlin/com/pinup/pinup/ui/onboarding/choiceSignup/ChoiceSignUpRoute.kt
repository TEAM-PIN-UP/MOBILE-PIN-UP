package com.pinup.pinup.ui.onboarding.choiceSignup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.pinup.pinup.platform.ContextFactory
import com.pinup.pinup.ui.login.model.SNSUserInfo
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChoiceSignUpRoute(
    contextFactory: ContextFactory,
    onMoveLogin : () -> Unit,
    onMoveSignUp: (SNSUserInfo) -> Unit,
    viewModel: ChoiceSignUpViewModel = koinViewModel(parameters = { parametersOf(contextFactory) })
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                ChoiceSignUpUiEvent.MoveMain -> onMoveLogin() // TODO Main 구현되면 수정할 예정
                is ChoiceSignUpUiEvent.MoveSignUp -> onMoveSignUp(it.snsLoginInfo)
                ChoiceSignUpUiEvent.MoveEmailLogin -> onMoveLogin()
            }
        }
    }
    ChoiceSignUpScreen(
        onClickSnsLogin = { type -> viewModel.doSNSLogin(type) }
    )
}