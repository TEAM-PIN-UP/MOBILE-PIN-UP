package com.pinup.placePinup.ui.onboarding.choiceSignup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.platform.ContextFactory
import com.pinup.placePinup.ui.component.LoadingDialog
import com.pinup.placePinup.ui.login.model.SNSUserInfo
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChoiceSignUpRoute(
    contextFactory: ContextFactory,
    onMoveLogin : () -> Unit,
    onMoveSignUp: (SNSUserInfo) -> Unit,
    onMoveMain: () -> Unit,
    viewModel: ChoiceSignUpViewModel = koinViewModel(parameters = { parametersOf(contextFactory) })
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                ChoiceSignUpUiEvent.MoveMain -> onMoveMain()
                is ChoiceSignUpUiEvent.MoveSignUp -> onMoveSignUp(it.snsLoginInfo)
                ChoiceSignUpUiEvent.MoveEmailLogin -> onMoveLogin()
            }
        }
    }
    ChoiceSignUpScreen(
        onClickSnsLogin = { type -> viewModel.doSNSLogin(type) }
    )

    if (isLoading) {
        LoadingDialog()
    }
}