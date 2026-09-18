package com.pinup.placePinup.domain.model

data class PagingNotification(
    val content: List<Notification> = emptyList(),
    val last: Boolean = true,
    val totalPages: Int = 0,
    val unReadCount: Int = 0,
)

data class Notification(
    val id: Int = -1,
    val title: String = "",
    val message: String = "",
    val type: FCMType = FCMType.DAILY_LOG_REMINDER,
    val targetId: Int = -1,
    val senderProfileImage: String? = "",
    val imageUrl: String? = "",
    val isRead: Boolean = false
)
