package com.pinup.pinup.remote.api

import com.pinup.pinup.data.response.GetMemberInfoResponse
import com.pinup.pinup.data.response.GetReviewsResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.data.response.SearchUserResponse
import com.pinup.pinup.domain.model.PResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.parameters

class MembersApi(
    private val httpClient: HttpClient
) {
    suspend fun checkNickName(nickname: String): PResult<PResponse<Boolean>> {
        val response = httpClient.get("api/members/nickname/check") {
            url {
                parameters.append("nickname", nickname)
            }
        }
        return response.body()
    }
    suspend fun searchUser(nickname: String): PResult<PResponse<List<SearchUserResponse>>> {
        val response = httpClient.get("api/members/search") {
            url {
                parameters.append("nickname", nickname)
            }
        }
        return response.body()
    }
    suspend fun getMemberInfo(memberId: Int): PResult<PResponse<GetMemberInfoResponse>> {
        val response = httpClient.get("api/members/$memberId")
        return response.body()
    }
    suspend fun getMemberInfo(): PResult<PResponse<GetMemberInfoResponse>> {
        val response = httpClient.get("api/members")
        return response.body()
    }
    suspend fun getTextReviews(
        memberId: Int,
        page: Int,
        size: Int,
    ): PResult<PResponse<GetReviewsResponse>> {
        val response = httpClient.get("api/members/$memberId/text-reviews") {
            url {
                parameters {
                    append("page", page.toString())
                    append("size", size.toString())
                }
            }
        }
        return response.body()
    }
    suspend fun getTextReviews(
        page: Int,
        size: Int,
    ): PResult<PResponse<GetReviewsResponse>> {
        val response = httpClient.get("api/members/me/text-reviews") {
            url {
                parameters {
                    append("page", page.toString())
                    append("size", size.toString())
                }
            }
        }
        return response.body()
    }
    suspend fun getPhotoReviews(
        memberId: Int,
        page: Int,
        size: Int,
    ): PResult<PResponse<GetReviewsResponse>> {
        val response = httpClient.get("api/members/$memberId/photo-reviews") {
            url {
                parameters {
                    append("page", page.toString())
                    append("size", size.toString())
                }
            }
        }
        return response.body()
    }
    suspend fun getPhotoReviews(
        page: Int,
        size: Int,
    ): PResult<PResponse<GetReviewsResponse>> {
        val response = httpClient.get("api/members/me/photo-reviews") {
            url {
                parameters {
                    append("page", page.toString())
                    append("size", size.toString())
                }
            }
        }
        return response.body()
    }
}