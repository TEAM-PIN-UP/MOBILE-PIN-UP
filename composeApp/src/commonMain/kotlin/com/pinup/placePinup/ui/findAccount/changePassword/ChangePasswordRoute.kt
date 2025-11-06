package com.pinup.placePinup.ui.findAccount.changePassword

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
    viewModel: ChangePasswordViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it){
                ChangePasswordUiEvent.MoveLogin -> onMoveLogin
            }
        }
    }

    ChangePasswordScreen(
        password = uiState.password,
        passwordAgain = uiState.passwordAgain,
        isPasswordValid = uiState.isPasswordValid,
        isPasswordMatched = uiState.isPasswordMatched,
        isShowFirstPassword = uiState.isShowFirstPassword,
        isShowPassword = uiState.isShowPassword,
        onPasswordChanged = viewModel::updatePassword,
        onPasswordAgainChanged = viewModel::updatePasswordAgain,
        onClickShowFirstPassword = viewModel::onClickShowFirstPassword,
        onClickShowPassword = viewModel::onClickShowPassword,
        onClickConfirm = viewModel::changePassword,
        onBackPressed = onBackPressed
    )
}