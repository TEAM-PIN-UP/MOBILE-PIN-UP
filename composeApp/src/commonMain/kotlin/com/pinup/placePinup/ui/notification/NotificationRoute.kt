package com.pinup.placePinup.ui.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.domain.model.FCMType
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationRoute(
    onBackPressed: () -> Unit = {},
    viewModel: NotificationViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getNotification()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when(it) {
                is NotificationUiEvent.OnMoveUiEvent -> {
                    when(it.type) {
                        FCMType.PLACE -> {}
                        FCMType.PINLOG -> {}
                        FCMType.PINBUDDY -> {}
                        FCMType.USER -> {}
                        FCMType.MY_PROFILE -> {}
                        FCMType.NONE -> {}
                    }
                }
            }
        }
    }

    NotificationScreen(
        notificationList = uiState.pagingNotification.content,
        isRefreshing = uiState.isRefreshing,
        getMoreNotification = viewModel::getMoreNotification,
        onRefresh = viewModel::refreshView,
        onClickBack = onBackPressed,
        onClickNotification = viewModel::readNotification
    )
}
