package com.pinup.placePinup.domain.repository

import com.pinup.placePinup.data.request.fcm.SetDeviceTokenRequest
import com.pinup.placePinup.domain.model.PResult

interface CallRepository {
    suspend fun setDeviceToken(request: SetDeviceTokenRequest): PResult<Unit>
    suspend fun deleteDeviceToken(token: String): PResult<Unit>
    suspend fun readNotification(path: Int): PResult<Unit>
    suspend fun readAllNotification(): PResult<Unit>
    suspend fun getMyNotification(): PResult<Unit>
    suspend fun deleteNotification(path: Int): PResult<Unit>
}