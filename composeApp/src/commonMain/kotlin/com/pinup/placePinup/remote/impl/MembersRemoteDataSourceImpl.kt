package com.pinup.placePinup.remote.impl

import com.pinup.placePinup.data.remote.MembersRemoteDataSource
import com.pinup.placePinup.data.request.ProfileEditRequest
import com.pinup.placePinup.data.response.CheckNickNameResponse
import com.pinup.placePinup.data.response.FindIdResponse
import com.pinup.placePinup.data.response.GetMemberInfoResponse
import com.pinup.placePinup.data.response.GetReviewsResponse
import com.pinup.placePinup.data.response.SearchUserResponse
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.remote.api.MembersApi


class MembersRemoteDataSourceImpl (
    private val membersApi: MembersApi
): MembersRemoteDataSource {

    override suspend fun checkNickName(nickname: String): PResult<CheckNickNameResponse> {
        return membersApi.checkNickName(nickname).mapSuccessData()
    }

    override suspend fun searchUser(nickname: String): PResult<List<SearchUserResponse>> {
        return membersApi.searchUser(nickname).mapSuccessData()
    }

    override suspend fun getMemberInfo(memberId: Int?): PResult<GetMemberInfoResponse> {
        return memberId?.let {
            membersApi.getMemberInfo(it).mapSuccessData()
        } ?: membersApi.getMemberInfo().mapSuccessData()
    }

    override suspend fun getTextReviews(
        memberId: Int?,
        page: Int,
        size: Int
    ): PResult<GetReviewsResponse> {
        return memberId?.let {
            membersApi.getTextReviews(it, page, size).mapSuccessData()
        } ?: membersApi.getTextReviews(page, size).mapSuccessData()
    }

    override suspend fun getPhotoReviews(
        memberId: Int?,
        page: Int,
        size: Int
    ): PResult<GetReviewsResponse> {
        return memberId?.let {
            membersApi.getPhotoReviews(it, page, size).mapSuccessData()
        } ?: membersApi.getPhotoReviews(page, size).mapSuccessData()
    }

    override suspend fun editProfile(request: ProfileEditRequest): PResult<Unit> {
        return membersApi.editProfile(request).mapSuccessData()
    }

    override suspend fun unregister(): PResult<Unit> {
        return membersApi.unregister().mapSuccessData()
    }

    override suspend fun findIdByEmail(request: String): PResult<FindIdResponse> {
        return membersApi.getFindIdByEmail(request).mapSuccessData()
    }

    override suspend fun findIdByNickName(request: String): PResult<FindIdResponse> {
        return membersApi.getFindIdByNickname(request).mapSuccessData()
    }
}