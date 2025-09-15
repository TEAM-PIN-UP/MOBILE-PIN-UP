package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.MembersRemoteDataSource
import com.pinup.pinup.data.request.ProfileEditRequest
import com.pinup.pinup.data.response.CheckNickNameResponse
import com.pinup.pinup.data.response.FindIdResponse
import com.pinup.pinup.data.response.GetMemberInfoResponse
import com.pinup.pinup.data.response.GetReviewsResponse
import com.pinup.pinup.data.response.SearchUserResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.MembersApi


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