package com.pinup.pinup.ui.findAccount.findPassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChangePasswordRoute(
    onMoveLogin : () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: FindPasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it){
                //TODO 비밀번호 변경에 선공하면 login으로 가지는 로직
            }
        }
    }

    ChangePasswordScreen(
        passwordState = uiState.passwordState,
        onPasswordChanged = viewModel::updatePassword,
        onPasswordAgainChanged = viewModel::updatePasswordAgain,
        onClickShowPassword = viewModel::onClickShowPassword,
        onClickConfirm = viewModel::changePassword,
        onBackPressed = onBackPressed
    )
}