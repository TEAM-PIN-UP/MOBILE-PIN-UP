package com.pinup.placePinup.ui.notification

import androidx.lifecycle.viewModelScope
import com.pinup.placePinup.domain.model.FCMType
import com.pinup.placePinup.domain.model.Notification
import com.pinup.placePinup.domain.model.PagingNotification
import com.pinup.placePinup.domain.model.PagingReview
import com.pinup.placePinup.domain.usecase.DeletePinlogUseCase
import com.pinup.placePinup.domain.usecase.DeleteRecentSearchUseCase
import com.pinup.placePinup.domain.usecase.GetFeedUseCase
import com.pinup.placePinup.domain.usecase.GetMyProfileUseCase
import com.pinup.placePinup.domain.usecase.GetNotificationUseCase
import com.pinup.placePinup.domain.usecase.GetPinlogDetailUseCase
import com.pinup.placePinup.domain.usecase.GetRecentSearchUseCase
import com.pinup.placePinup.domain.usecase.PostReadNotificationUseCase
import com.pinup.placePinup.domain.usecase.PostReviewLikeChangeUseCase
import com.pinup.placePinup.domain.usecase.SaveRecentSearchUseCase
import com.pinup.placePinup.ui.base.BaseViewModel
import com.pinup.placePinup.ui.base.UiEvent
import com.pinup.placePinup.ui.base.UiState
import com.pinup.placePinup.ui.main.compose.MainDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class NotificationViewModel(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val postReadNotificationUseCase: PostReadNotificationUseCase,
) : BaseViewModel<NotificationUiState, UiEvent>(NotificationUiState()) {

    private var currentPage = 0

    fun getNotification() = viewModelScope.launch {
        resultResponse(
            response = getNotificationUseCase(currentPage),
            successCallback = {
                currentPage++
                updateState {
                    copy(
                        pagingNotification = it,
                        isRefreshing = false
                    )
                }
            }
        )
    }

    fun getMoreNotification() = viewModelScope.launch {
        if (uiState.value.pagingNotification.last) return@launch
        resultResponse(
            response = getNotificationUseCase(currentPage),
            successCallback = {
                currentPage++
                updateState {
                    copy(
                        pagingNotification = uiState.value.pagingNotification.copy(
                            content = uiState.value.pagingNotification.content + it.content
                        )
                    )
                }
            }
        )
    }

    fun refreshView() {
        updateState {
            copy(
                isRefreshing = true
            )
        }
        currentPage = 0
        getNotification()
    }

    fun readNotification(notification: Notification) = viewModelScope.launch {
        resultResponse(
            response = postReadNotificationUseCase(notification.id),
            successCallback = {
                emitEvent(NotificationUiEvent.OnMoveUiEvent(notification.type, -1)) // TODO 데이터에서 targetId 뽑기
            }
        )
    }
}

data class NotificationUiState(
    val pagingNotification: PagingNotification = PagingNotification(),
    val isRefreshing: Boolean = false,
): UiState

sealed class NotificationUiEvent : UiEvent {
    data class OnMoveUiEvent(val type: FCMType, val targetId: Int): NotificationUiEvent()
}