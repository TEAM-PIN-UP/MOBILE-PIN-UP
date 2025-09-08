package com.pinup.pinup.ui.login.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.platform.ContextFactory
import org.koin.compose.viewmodel.koinViewModel
import com.pinup.pinup.ui.login.LoginUiEvent
import com.pinup.pinup.ui.login.LoginViewModel
import com.pinup.pinup.ui.login.model.SNSType
import com.pinup.pinup.ui.login.model.SNSUserInfo
import com.pinup.pinup.ui.map.MapViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

@Composable
fun LoginRoute(
    contextFactory: ContextFactory,
    onMoveSignUp: (SNSUserInfo) -> Unit,
    onMoveMain: () -> Unit,
    onMoveFindId: () -> Unit,
    onMoveFindPassword: () -> Unit,
    viewModel: LoginViewModel = koinViewModel(parameters = { parametersOf(contextFactory) })
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                LoginUiEvent.MoveMain -> onMoveMain()
                is LoginUiEvent.MoveSignUp -> onMoveSignUp(it.snsLoginInfo)
            }
        }
    }
    LoginScreen(
        onSnsLoginClick = viewModel::doSNSLogin,
        onIdChanged = viewModel::onIdChange,
        onPasswordChanged = viewModel::onPasswordChange,
        onClickFindPassword = onMoveFindPassword,
        onCLickFindEmail = onMoveFindId,
        id = uiState.id,
        password = uiState.password,
        isError = uiState.isError

    )
}