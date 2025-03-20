package com.pinup.pinup.ui.login.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.pinup.pinup.ui.login.LoginUiEvent
import com.pinup.pinup.ui.login.LoginViewModel
import com.pinup.pinup.ui.login.model.SNSUserInfo
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginRoute(
    onMoveSignUp: (SNSUserInfo) -> Unit,
    onMoveMain: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
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