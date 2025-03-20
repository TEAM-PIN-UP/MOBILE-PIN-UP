package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.RequestPinBuddyRequest
import com.pinup.pinup.data.response.GetPinBuddiesResponse
import com.pinup.pinup.data.response.GetPinBuddyRequestsResponse
import com.pinup.pinup.data.response.MemberResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

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