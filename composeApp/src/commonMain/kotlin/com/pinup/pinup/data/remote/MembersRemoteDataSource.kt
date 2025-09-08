package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.ProfileEditRequest
import com.pinup.pinup.data.response.CheckNickNameResponse
import com.pinup.pinup.data.response.GetMemberInfoResponse
import com.pinup.pinup.data.response.GetReviewsResponse
import com.pinup.pinup.data.response.SearchUserResponse
import com.pinup.pinup.domain.model.PResult

interface MembersRemoteDataSource {
    suspend fun checkNickName(nickname: String): PResult<CheckNickNameResponse>
    suspend fun searchUser(nickname: String): PResult<List<SearchUserResponse>>
    suspend fun getMemberInfo(memberId: Int?): PResult<GetMemberInfoResponse>
    suspend fun getTextReviews(memberId: Int?, page: Int, size: Int): PResult<GetReviewsResponse>
    suspend fun getPhotoReviews(memberId: Int?, page: Int, size: Int): PResult<GetReviewsResponse>
    suspend fun editProfile(request: ProfileEditRequest): PResult<Unit>
    suspend fun unregister(): PResult<Unit>
}