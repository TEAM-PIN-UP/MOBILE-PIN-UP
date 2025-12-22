package com.pinup.placePinup.remote.impl

import com.pinup.placePinup.data.remote.CallRemoteDataSource
import com.pinup.placePinup.data.request.fcm.SetDeviceTokenRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.remote.api.CallApi

class CallRemoteDataSourceImpl(
    private val callApi: CallApi
) : CallRemoteDataSource {
    override suspend fun setDeviceToken(request: SetDeviceTokenRequest): PResult<Unit> {
        return callApi.setDeviceToken(request).mapSuccessData()
    }

    override suspend fun deleteDeviceToken(token: String): PResult<Unit> {
        return callApi.deleteDeviceToken(token).mapSuccessData()
    }

    override suspend fun readNotification(path: Int): PResult<Unit> {
        return callApi.readNotification(path).mapSuccessData()
    }

    override suspend fun readAllNotification(): PResult<Unit> {
        return callApi.readAllNotification().mapSuccessData()
    }

    override suspend fun getMyNotification(): PResult<Unit> {
        return callApi.getMyNotification().mapSuccessData()
    }

    override suspend fun deleteNotification(path: Int): PResult<Unit> {
        return callApi.deleteNotification(path).mapSuccessData()
    }
}