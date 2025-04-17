package com.pinup.pinup.ui.login.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.pinup.pinup.platform.ContextFactory
import org.koin.compose.viewmodel.koinViewModel
import com.pinup.pinup.ui.login.LoginUiEvent
import com.pinup.pinup.ui.login.LoginViewModel
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.map.MapViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

@Composable
fun LoginRoute(
    contextFactory: ContextFactory,
    onMoveSignUp: (SNSUserInfo) -> Unit,
    onMoveMain: () -> Unit,
) {
    val loginViewModel: LoginViewModel = koinViewModel(parameters = { parametersOf(contextFactory) })
    LaunchedEffect(Unit) {
        loginViewModel.uiEvent.collectLatest {
            when(it) {
                LoginUiEvent.MoveMain -> onMoveMain()
                is LoginUiEvent.MoveSignUp -> onMoveSignUp(it.snsLoginInfo)
            }
        }
    }
    LoginScreen(
        onSnsLoginClick = loginViewModel::doSNSLogin
    )
}