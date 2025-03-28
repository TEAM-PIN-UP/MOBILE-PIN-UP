package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.RequestPinBuddyRequest
import com.pinup.pinup.data.response.GetPinBuddiesResponse
import com.pinup.pinup.data.response.GetPinBuddyRequestsResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.parameters

class PinBuddyApi(
    private val httpClient: HttpClient
) {
    suspend fun requestPinBuddy(request: RequestPinBuddyRequest): PResult<PResponse<Unit>> {
        val response = httpClient.post("api/friend-requests/send") {
            setBody(request)
        }
        return response.body()
    }

    suspend fun deleteRequestPinBuddy(friendRequestId: Int): PResult<PResponse<Unit>> {
        val response = httpClient.delete("api/friend-requests/$friendRequestId")
        return response.body()
    }

    suspend fun rejectPinBuddy(friendRequestId: Int): PResult<PResponse<Unit>> {
        val response = httpClient.patch("api/friend-requests/$friendRequestId/reject")
        return response.body()
    }

    suspend fun acceptPinBuddy(friendRequestId: Int): PResult<PResponse<Unit>> {
        val response = httpClient.patch("api/friend-requests/$friendRequestId/accept")
        return response.body()
    }

    suspend fun getReceivedPinBuddyRequests(page: Int, size: Int): PResult<PResponse<GetPinBuddyRequestsResponse>> {
        val response = httpClient.get("api/friend-requests/received") {
            url {
                parameters {
                    append("page", page.toString())
                    append("size", size.toString())
                }
            }
        }
        return response.body()
    }

    suspend fun getSentPinBuddyRequests(page: Int, size: Int): PResult<PResponse<GetPinBuddyRequestsResponse>> {
        val response = httpClient.get("api/friend-requests/sent") {
            url {
                parameters {
                    append("page", page.toString())
                    append("size", size.toString())
                }
            }
        }
        return response.body()
    }

    suspend fun getPinBuddies(page: Int, size: Int): PResult<PResponse<GetPinBuddiesResponse>> {
        val response = httpClient.get("api/friendships/me") {
            url {
                parameters {
                    append("page", page.toString())
                    append("size", size.toString())
                }
            }
        }
        return response.body()
    }

    suspend fun deletePinBuddy(friendId: String): PResult<PResponse<Unit>> {
        val response = httpClient.post("api/friendships/$friendId")
        return response.body()
    }
}