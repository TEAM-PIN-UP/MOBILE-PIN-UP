package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.PinBuddyRemoteDataSource
import com.pinup.pinup.data.request.RequestPinBuddyRequest
import com.pinup.pinup.data.response.GetPinBuddiesResponse
import com.pinup.pinup.data.response.GetPinBuddyRequestsResponse
import com.pinup.pinup.data.response.MemberResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.PinBuddyApi


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