package com.pinup.pinup.ui.findAccount.findId

import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FindIdRoute(
    onMoveLogin: () -> Unit,
    onMoveFindPassword: () -> Unit = {},
    onBackPressed: () -> Unit,
    viewModel: FindIdViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden
    )

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                FindIdUiEvent.ShowInfoBottomSheet -> sheetState.show()
            }
        }
    }

    FindIdScreen(
        email = uiState.email,
        verificationCode = uiState.verificationCode,
        isEmailValid = uiState.isEmailValid,
        emailVerifyType = uiState.emailVerifyType,
        isVerifyClicked = uiState.isVerifyClicked,
        sheetState = sheetState,
        findProfileUrl = uiState.findProfileUrl,
        findNickName = uiState.findNickName,
        onClickSendCode = viewModel::onClickVerify,
        onClickVerify = viewModel::verifyEmail,
        onEmailChanged = viewModel::updateEmail,
        onCodeChanged = viewModel::updateVerificationCode,
        onBackPressed = onBackPressed,
        onClickTabChanged = viewModel::updateTabState,
        isEmailFindClicked = uiState.isFindByEmail,
        onClickLogin = onMoveLogin,
        onClickFindPassword = onMoveFindPassword,
        onClickConfirm = {},
    )
}