package com.pinup.placePinup.data.impl

import com.pinup.placePinup.data.remote.CallRemoteDataSource
import com.pinup.placePinup.data.request.fcm.SetDeviceTokenRequest
import com.pinup.placePinup.data.response.NotificationResponse.Companion.toModel
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingNotification
import com.pinup.placePinup.domain.model.map
import com.pinup.placePinup.domain.repository.CallRepository

class CallRepositoryImpl (
    private val callRemoteDataSource: CallRemoteDataSource,
) : CallRepository {
    override suspend fun setDeviceToken(request: SetDeviceTokenRequest): PResult<Unit> {
        return callRemoteDataSource.setDeviceToken(request)
    }

    override suspend fun deleteDeviceToken(token: String): PResult<Unit> {
        return callRemoteDataSource.deleteDeviceToken(token)
    }

    override suspend fun readNotification(path: Int): PResult<Unit> {
        return callRemoteDataSource.readNotification(path)
    }

    override suspend fun readAllNotification(): PResult<Unit> {
        return callRemoteDataSource.readAllNotification()
    }

    override suspend fun getMyNotification(page: Int): PResult<PagingNotification> {
        return callRemoteDataSource.getMyNotification(page).map { it.toModel() }
    }

    override suspend fun deleteNotification(path: Int): PResult<Unit> {
        return callRemoteDataSource.deleteNotification(path)
    }
}