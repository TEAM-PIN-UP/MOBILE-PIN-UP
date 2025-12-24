package com.pinup.placePinup.ui.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationRoute(
    onBackPressed: () -> Unit = {},
    viewModel: NotificationViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        viewModel.getFeedList()
//        viewModel.getMyProfile()
//    }
//
//    LaunchedEffect(Unit) {
//        viewModel.uiEvent.collectLatest {
//            when(it) {
//                is FeedUiEvent.OnMoveUserProfile -> {
//                    onMoveUserProfile(it.name)
//                }
//            }
//        }
//    }

    NotificationScreen(
        notificationList = uiState.pagingNotification.content,
        isRefreshing = uiState.isRefreshing,
        getMoreNotification = viewModel::getNotification,
        onRefresh = viewModel::refreshView,
        onClickBack = onBackPressed
    )
}
