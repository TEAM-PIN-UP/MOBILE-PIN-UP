package com.pinup.pinup.ui.findAccount.findPassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.pinup.platform.ContextFactory
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChangePasswordRoute(
    onMoveLogin : () -> Unit,
    viewModel: FindPasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it){

            }
        }
    }

    ChangePasswordScreen(
        passwordState = uiState.passwordState,
        onPasswordChanged = viewModel::updatePassword,
        onPasswordAgainChanged = viewModel::updatePasswordAgain,
        onClickShowPassword = viewModel::onClickShowPassword,
        onClickConfirm = viewModel::changePassword
    )
}