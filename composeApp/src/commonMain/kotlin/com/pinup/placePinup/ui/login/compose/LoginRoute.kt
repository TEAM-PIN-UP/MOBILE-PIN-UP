package com.pinup.placePinup.ui.login.compose

import PToastHost
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.platform.ContextFactory
import org.koin.compose.viewmodel.koinViewModel
import com.pinup.placePinup.ui.login.LoginUiEvent
import com.pinup.placePinup.ui.login.LoginViewModel
import com.pinup.placePinup.ui.login.model.SNSType
import com.pinup.placePinup.ui.login.model.SNSUserInfo
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf
import rememberToastState

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
    val toast = rememberToastState()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                LoginUiEvent.MoveMain -> onMoveMain()
                is LoginUiEvent.MoveSignUp -> onMoveSignUp(it.snsLoginInfo)
                is LoginUiEvent.TestError -> {
                    toast.show(it.test)
                }
            }
        }
    }
    LoginScreen(
        onSnsLoginClick = viewModel::doSNSLogin,
        onIdChanged = viewModel::onIdChange,
        onPasswordChanged = viewModel::onPasswordChange,
        onClickFindPassword = onMoveFindPassword,
        onCLickFindEmail = onMoveFindId,
        onMoveSignUp = { onMoveSignUp(SNSUserInfo(snsType = SNSType.PINUP)) },
        id = uiState.id,
        password = uiState.password,
        isError = uiState.isError

    )

    PToastHost(
        modifier = Modifier
            .statusBarsPadding()
            .padding(top = 24.dp, start = 20.dp, end = 20.dp),
        state = toast
    )

}