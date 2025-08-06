package com.pinup.pinup.ui.findAccount.findPassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FindPasswordEmailRoute(
    onMoveChangePassword: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel: FindPasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it){

            }
        }
    }

    FindPasswordEmailScreen(
        onMovePassword = onMoveChangePassword,
        emailState = uiState.emailState,
        onEmailChanged = viewModel::updateEmail,
        onCodeChanged = viewModel::updateVerificationCode,
        onClickSendCode = viewModel::onClickSendCode,
        onClickVerify = viewModel::onClickVerify,
        onBackPressed = onBackPressed
    )
}