package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.RequestPinBuddyRequest
import com.pinup.pinup.data.response.GetPinBuddiesResponse
import com.pinup.pinup.data.response.GetPinBuddyRequestsResponse
import com.pinup.pinup.data.response.MemberResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PinBuddyApi {
    @POST("api/friend-requests/send")
    suspend fun requestPinBuddy(@Body request: RequestPinBuddyRequest): PResult<PResponse<Unit>>

    @DELETE("api/friend-requests/{friendRequestId}")
    suspend fun deleteRequestPinBuddy(@Path("friendRequestId") friendRequestId: Int): PResult<PResponse<Unit>>

    @PATCH("api/friend-requests/{friendRequestId}/reject")
    suspend fun rejectPinBuddy(@Path("friendRequestId") friendRequestId: Int): PResult<PResponse<Unit>>

    @PATCH("api/friend-requests/{friendRequestId}/accept")
    suspend fun acceptPinBuddy(@Path("friendRequestId") friendRequestId: Int): PResult<PResponse<Unit>>

    @GET("api/friend-requests/received")
    suspend fun getReceivedPinBuddyRequests(@Query("page") page: Int, @Query("size") size: Int): PResult<PResponse<GetPinBuddyRequestsResponse>>

    @GET("api/friend-requests/sent")
    suspend fun getSentPinBuddyRequests(@Query("page") page: Int, @Query("size") size: Int): PResult<PResponse<GetPinBuddyRequestsResponse>>

    @GET("api/friendships/me")
    suspend fun getPinBuddies(@Query("page") page: Int, @Query("size") size: Int): PResult<PResponse<GetPinBuddiesResponse>>

    @DELETE("api/friendships/{friendId}")
    suspend fun deletePinBuddy(@Path("friendId") friendId: String): PResult<PResponse<Unit>>
}