package com.pinup.pinup.ui.setting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingRoute(
    viewModel: SettingViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    onMoveLoginScreen: () -> Unit,
    onMoveProfileModify: () -> Unit,
    onMoveUnRegister: () -> Unit = {},
    onChangedPassword: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                SettingUiEvent.MoveLogin -> onMoveLoginScreen()
            }
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    SettingScreen(
        email = uiState.value.email,
        snsType = uiState.value.snsType,
        onBackPressed = onBackPressed,
        onLogoutClick = viewModel::logout,
        onProfileModifyClick = onMoveProfileModify,
        onChangedPassword = onChangedPassword,
        onMoveUnRegister = onMoveUnRegister
    )
}