package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.request.RequestPinBuddyRequest
import com.pinup.placePinup.data.response.GetPinBuddiesResponse
import com.pinup.placePinup.data.response.GetPinBuddyRequestsResponse
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

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