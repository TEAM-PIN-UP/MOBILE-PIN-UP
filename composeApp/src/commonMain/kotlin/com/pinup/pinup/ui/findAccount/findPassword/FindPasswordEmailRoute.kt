package com.pinup.pinup.ui.findAccount.findPassword

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FindPasswordEmailRoute(
    onMoveLogin: () -> Unit,
    onBackPressed: () -> Unit,
    viewModel:FindPasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isSentPassword) {
        FindPasswordEmailScreen(
            email = uiState.email,
            isEmailValid = uiState.isEmailValid,
            onEmailChanged = viewModel::updateEmail,
            onClickSendPassword = viewModel::onClickSendPassword,
            onBackPressed = onBackPressed
        )
    } else {
        SentPasswordScreen(
            email = uiState.email,
            onBackPressed = viewModel::onClickBack,
            onClickLogin = onMoveLogin
        )
    }
}