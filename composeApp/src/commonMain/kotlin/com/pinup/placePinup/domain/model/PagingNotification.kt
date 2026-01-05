package com.pinup.placePinup.domain.model

data class PagingNotification(
    val content: List<Notification> = emptyList(),
    val last: Boolean = true
)

data class Notification(
    val id: Int = -1,
    val title: String = "",
    val message: String = "",
    val type: FCMType = FCMType.NONE,
    val targetId: Int = -1,
    val senderProfileImage: String? = "",
    val imageUrl: String? = "",
    val isRead: Boolean = false
)
