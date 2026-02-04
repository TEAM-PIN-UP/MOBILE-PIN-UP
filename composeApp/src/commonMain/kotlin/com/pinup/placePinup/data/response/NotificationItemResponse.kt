package com.pinup.placePinup.data.response

import com.pinup.placePinup.data.response.NotificationContentResponse.Companion.toModel
import com.pinup.placePinup.domain.model.FCMType
import com.pinup.placePinup.domain.model.Notification
import com.pinup.placePinup.domain.model.PagingNotification
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
data class NotificationResponse(
    val notifications: NotificationItemResponse = NotificationItemResponse(),
    val unReadCount: Int = 0
) {
    companion object Companion {
        fun NotificationResponse.toModel(): PagingNotification {
            return PagingNotification(
                content = notifications.content.map {
                    it.toModel()
                },
                last = notifications.last,
                totalPages = notifications.totalPages,
                unReadCount = unReadCount
            )
        }
    }
}

@Serializable
data class NotificationItemResponse(
    val content: List<NotificationContentResponse> = emptyList(),
    val last: Boolean = false,
    val totalPages: Int = 0,
)

@Serializable
data class NotificationContentResponse(
    val id: Int = -1,
    val title: String = "",
    val message: String = "",
    val type: String = "",
    val targetId: String? = "",
    val senderProfileImage: String? = "",
    val imageUrl: String? = "",
    val createdAt: List<Int> = emptyList(),
    val readAt: List<Int>? = emptyList(),
    val isRead: Boolean = false
) {
    companion object Companion {
        fun NotificationContentResponse.toModel(): Notification {
            return Notification(
                id = id,
                title = title,
                message = message,
                type = FCMType.of(type),
                targetId = targetId?.toIntOrNull() ?: -1,
                senderProfileImage = senderProfileImage,
                imageUrl = imageUrl,
                isRead = isRead
            )
        }
    }
}