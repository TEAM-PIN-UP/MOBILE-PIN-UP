package com.pinup.placePinup.ui.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pinup.placePinup.domain.model.FCMType
import com.pinup.placePinup.ui.main.compose.MainDestination
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NotificationRoute(
    onBackPressed: () -> Unit = {},
    onMovePlaceDetail: (String) -> Unit = {},
    onMovePinlogDetail: (Int) -> Unit = {},
    onMovePinBuddy: () -> Unit = {},
    onMoveUserProfileWithId: (Int) -> Unit = {},
    onMoveMyProfile: () -> Unit = {},
    viewModel: NotificationViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initNotification()
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is NotificationUiEvent.OnMoveUiEvent -> {
                    when (it.type) {
                        FCMType.FRIEND_LOG_SAME_PLACE,
                        FCMType.FRIEND_LOG_CREATED -> onMovePlaceDetail(it.targetId.toString())

                        FCMType.MEMORY_REMINDER,
                        FCMType.LOG_LIKE,
                        FCMType.LOG_COMMENT -> onMovePinlogDetail(it.targetId)

                        FCMType.FRIEND_REQUEST -> onMovePinBuddy()
                        FCMType.FRIEND_REQUEST_ACCEPTED -> onMoveUserProfileWithId(it.targetId)
                        FCMType.SUMMARY_WEEKLY,
                        FCMType.SUMMARY_MONTHLY -> onMoveMyProfile()

                        FCMType.FEATURE_UPDATE,
                        FCMType.ANNIVERSARY,
                        FCMType.WEEKLY_RECOMMENDATION, //TODO 아티클로 보내는듯
                        FCMType.DAILY_LOG_REMINDER,
                        FCMType.WEEKLY_LOG_REMINDER,
                        FCMType.DORMANT_USER_REENGAGEMENT -> {}
                    }
                }
            }
        }
    }

    NotificationScreen(
        unReadCount = uiState.pagingNotification.unReadCount,
        notificationList = uiState.pagingNotification.content,
        isRefreshing = uiState.isRefreshing,
        getMoreNotification = viewModel::getMoreNotification,
        onRefresh = viewModel::refreshView,
        onClickBack = onBackPressed,
        onClickNotification = viewModel::readNotification
    )
}
