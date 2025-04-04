package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.RequestPinBuddyRequest
import com.pinup.pinup.data.response.GetPinBuddiesResponse
import com.pinup.pinup.data.response.GetPinBuddyRequestsResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.parameters

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