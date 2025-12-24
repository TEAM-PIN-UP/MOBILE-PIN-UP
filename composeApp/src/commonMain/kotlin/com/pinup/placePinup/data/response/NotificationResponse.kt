package com.pinup.placePinup.data.response

import com.pinup.placePinup.data.response.NotificationItemResponse.Companion.toModel
import com.pinup.placePinup.domain.model.FCMType
import com.pinup.placePinup.domain.model.Notification
import com.pinup.placePinup.domain.model.PagingNotification
import kotlinx.serialization.Serializable

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
    val data: String = "",
    val isRead: Boolean = false
) {
    companion object {
        fun NotificationItemResponse.toModel(): Notification {
            return Notification(
                id = id,
                title = title,
                message = message,
                type = FCMType.of(type),
                data = data,
                isRead = isRead
            )
        }
    }
}