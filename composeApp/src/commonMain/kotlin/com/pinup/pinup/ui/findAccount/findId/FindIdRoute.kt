package com.pinup.pinup.ui.findAccount.findId

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FindIdRoute(
    onMoveLogin: () -> Unit,
    onMoveFindPassword: () -> Unit = {},
    onBackPressed: () -> Unit,
    viewModel: FindIdViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FindIdScreen(
        email = uiState.email,
        verificationCode = uiState.verificationCode,
        isEmailValid = uiState.isEmailValid,
        emailVerifyType = uiState.emailVerifyType,
        isVerifyClicked = uiState.isVerifyClicked,
        onClickSendCode = viewModel::onClickVerify,
        onClickVerify = viewModel::verifyEmail,
        onEmailChanged = viewModel::updateEmail,
        onCodeChanged = viewModel::updateVerificationCode,
        onBackPressed = onBackPressed,
        onClickTabChanged = viewModel::updateTabState,
        isEmailFindClicked = uiState.isFindByEmail,
        onClickConfirm = {},
    )
}