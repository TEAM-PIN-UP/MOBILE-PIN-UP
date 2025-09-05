package com.pinup.pinup.ui.setting.unregister

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun UnRegisterRoute(
    viewModel: UnRegisterViewModel = koinViewModel(),
    onBackPressed: () -> Unit,
    onMoveCompleteUnRegister: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                UnRegisterUiEvent.MoveOnBoarding -> onMoveCompleteUnRegister()
            }
        }
    }

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    UnRegisterScreen(
        isCheck = uiState.value.isCheck,
        onClickCheck = viewModel::onClickCheck,
        onBackPressed = onBackPressed,
        onUnRegisterClick = viewModel::unregister
    )
}