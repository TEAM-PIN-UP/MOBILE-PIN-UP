package com.pinup.placePinup.domain.repository

import com.pinup.placePinup.data.request.fcm.SetDeviceTokenRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingNotification

interface CallRepository {
    suspend fun setDeviceToken(request: SetDeviceTokenRequest): PResult<Unit>
    suspend fun deleteDeviceToken(token: String): PResult<Unit>
    suspend fun readNotification(path: Int): PResult<Unit>
    suspend fun readAllNotification(): PResult<Unit>
    suspend fun getMyNotification(page: Int): PResult<PagingNotification>
    suspend fun deleteNotification(path: Int): PResult<Unit>
}