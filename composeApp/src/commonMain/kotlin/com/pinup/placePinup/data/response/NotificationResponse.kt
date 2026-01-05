package com.pinup.placePinup.data.response

import com.pinup.placePinup.data.response.NotificationItemResponse.Companion.toModel
import com.pinup.placePinup.domain.model.FCMType
import com.pinup.placePinup.domain.model.Notification
import com.pinup.placePinup.domain.model.PagingNotification
import kotlinx.serialization.Serializable
import kotlin.Int

@Serializable
data class NotificationResponse(
    val content: List<NotificationItemResponse> = emptyList(),
    val last: Boolean = false
) {
    companion object {
        fun NotificationResponse.toModel(): PagingNotification {
            return PagingNotification(
                content = content.map {
                    it.toModel()
                },
                last = last
            )
        }
    }
}

@Serializable
data class NotificationItemResponse(
    val id: Int = -1,
    val title: String = "",
    val message: String = "",
    val type: String = "",
    val targetId: Int? = -1,
    val senderProfileImage: String? = "",
    val imageUrl: String? = "",
    val createdAt: List<Int> = emptyList(),
    val readAt: List<Int>? = emptyList(),
    val isRead: Boolean = false
) {
    companion object {
        fun NotificationItemResponse.toModel(): Notification {
            return Notification(
                id = id,
                title = title,
                message = message,
                type = FCMType.of(type),
                targetId = targetId ?: -1,
                senderProfileImage = senderProfileImage,
                imageUrl = imageUrl,
                isRead = isRead
            )
        }
    }
}