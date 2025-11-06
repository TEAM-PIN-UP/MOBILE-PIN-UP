package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.ProfileEditRequest
import com.pinup.placePinup.data.response.CheckNickNameResponse
import com.pinup.placePinup.data.response.FindIdResponse
import com.pinup.placePinup.data.response.GetMemberInfoResponse
import com.pinup.placePinup.data.response.GetReviewsResponse
import com.pinup.placePinup.data.response.SearchUserResponse
import com.pinup.placePinup.domain.model.PResult

interface MembersRemoteDataSource {
    suspend fun checkNickName(nickname: String): PResult<CheckNickNameResponse>
    suspend fun searchUser(nickname: String): PResult<List<SearchUserResponse>>
    suspend fun getMemberInfo(memberId: Int?): PResult<GetMemberInfoResponse>
    suspend fun getTextReviews(memberId: Int?, page: Int, size: Int): PResult<GetReviewsResponse>
    suspend fun getPhotoReviews(memberId: Int?, page: Int, size: Int): PResult<GetReviewsResponse>
    suspend fun editProfile(request: ProfileEditRequest): PResult<Unit>
    suspend fun unregister(): PResult<Unit>
    suspend fun findIdByEmail(request: String): PResult<FindIdResponse>
    suspend fun findIdByNickName(request: String): PResult<FindIdResponse>
}