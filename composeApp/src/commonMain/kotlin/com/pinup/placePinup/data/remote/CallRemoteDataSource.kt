package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.fcm.SetDeviceTokenRequest
import com.pinup.placePinup.data.response.NotificationResponse
import com.pinup.placePinup.domain.model.PResult

interface CallRemoteDataSource {
    suspend fun setDeviceToken(request: SetDeviceTokenRequest): PResult<Unit>
    suspend fun deleteDeviceToken(token: String): PResult<Unit>
    suspend fun readNotification(path: Int): PResult<Unit>
    suspend fun readAllNotification(): PResult<Unit>
    suspend fun getMyNotification(page: Int): PResult<NotificationResponse>
    suspend fun deleteNotification(path: Int): PResult<Unit>
}