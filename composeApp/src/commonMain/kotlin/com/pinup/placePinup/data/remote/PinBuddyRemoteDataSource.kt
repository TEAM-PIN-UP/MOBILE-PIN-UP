package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.RequestPinBuddyRequest
import com.pinup.placePinup.data.response.GetPinBuddiesResponse
import com.pinup.placePinup.data.response.GetPinBuddyRequestsResponse
import com.pinup.placePinup.domain.model.PResult

interface PinBuddyRemoteDataSource {
    suspend fun getPinBuddies(page: Int, size: Int): PResult<GetPinBuddiesResponse>
    suspend fun getSentPinBuddyRequests(page: Int, size: Int): PResult<GetPinBuddyRequestsResponse>
    suspend fun getReceivedPinBuddyRequests(page: Int, size: Int): PResult<GetPinBuddyRequestsResponse>
    suspend fun deletePinBuddy(friendId: String): PResult<Unit>
    suspend fun requestPinBuddy(request: RequestPinBuddyRequest): PResult<Unit>
    suspend fun deleteRequestPinBuddy(friendRequestId: Int): PResult<Unit>
    suspend fun rejectPinBuddy(friendRequestId: Int): PResult<Unit>
    suspend fun acceptPinBuddy(friendRequestId: Int): PResult<Unit>
}