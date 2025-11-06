package com.pinup.placePinup.remote.impl

import com.pinup.placePinup.data.remote.PinBuddyRemoteDataSource
import com.pinup.placePinup.data.request.RequestPinBuddyRequest
import com.pinup.placePinup.data.response.GetPinBuddiesResponse
import com.pinup.placePinup.data.response.GetPinBuddyRequestsResponse
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.remote.api.PinBuddyApi


class PinBuddyRemoteDataSourceImpl (
    private val pinBuddyApi: PinBuddyApi,
) : PinBuddyRemoteDataSource {
    override suspend fun getPinBuddies(page: Int, size: Int): PResult<GetPinBuddiesResponse> {
        return pinBuddyApi.getPinBuddies(page, size).mapSuccessData()
    }

    override suspend fun getSentPinBuddyRequests(page: Int, size: Int): PResult<GetPinBuddyRequestsResponse> {
        return pinBuddyApi.getSentPinBuddyRequests(page, size).mapSuccessData()
    }

    override suspend fun getReceivedPinBuddyRequests(page: Int, size: Int): PResult<GetPinBuddyRequestsResponse> {
        return pinBuddyApi.getReceivedPinBuddyRequests(page, size).mapSuccessData()
    }

    override suspend fun deletePinBuddy(friendId: String): PResult<Unit> {
        return pinBuddyApi.deletePinBuddy(friendId).mapSuccessData()
    }

    override suspend fun requestPinBuddy(request: RequestPinBuddyRequest): PResult<Unit> {
        return pinBuddyApi.requestPinBuddy(request).mapSuccessData()
    }

    override suspend fun deleteRequestPinBuddy(friendRequestId: Int): PResult<Unit> {
        return pinBuddyApi.deleteRequestPinBuddy(friendRequestId).mapSuccessData()
    }

    override suspend fun rejectPinBuddy(friendRequestId: Int): PResult<Unit> {
        return pinBuddyApi.rejectPinBuddy(friendRequestId).mapSuccessData()
    }

    override suspend fun acceptPinBuddy(friendRequestId: Int): PResult<Unit> {
        return pinBuddyApi.acceptPinBuddy(friendRequestId).mapSuccessData()
    }
}