package com.pinup.pinup.data.impl

import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.data.remote.MembersRemoteDataSource
import com.pinup.pinup.data.request.ProfileEditRequest
import com.pinup.pinup.data.request.findId.FindByEmailRequest
import com.pinup.pinup.data.request.findId.FindByNickNameRequest
import com.pinup.pinup.data.response.CheckNickNameResponse.Companion.toModel
import com.pinup.pinup.data.response.FindIdResponse.Companion.toModel
import com.pinup.pinup.data.response.GetMemberInfoResponse.Companion.toModel
import com.pinup.pinup.data.response.GetReviewsResponse.Companion.toModel
import com.pinup.pinup.data.response.SearchUserResponse.Companion.toModel
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.model.map
import com.pinup.pinup.domain.repository.MembersRepository
import kotlinx.coroutines.flow.StateFlow

class MembersRepositoryImpl (
    private val membersRemoteDataSource: MembersRemoteDataSource,
    private val membersLocalDataSource: MembersLocalDataSource
): MembersRepository {
    override suspend fun checkNickName(nickname: String): PResult<Boolean> {
        return membersRemoteDataSource.checkNickName(nickname).map {
            it.toModel()
        }
    }

    override suspend fun saveToken(tokenInfo: TokenInfo): PResult<Unit> {
        return membersLocalDataSource.saveToken(tokenInfo)
    }

    override suspend fun getUserInfo(): StateFlow<UserInfo> {
        return membersLocalDataSource.getUserInfo()
    }

    override suspend fun getAccessToken(): String {
        return membersLocalDataSource.getAccessToken()
    }

    override suspend fun clearUserData() {
        membersLocalDataSource.logout()
    }

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        return membersLocalDataSource.saveUserInfo(userInfo)
    }

    override suspend fun searchUser(nickname: String): PResult<List<PinBuddy>> {
        return membersRemoteDataSource.searchUser(nickname)
            .map { response ->
                response.map {
                    it.toModel()
                }
            }
    }

    override suspend fun getMemberInfo(memberId: Int?): PResult<Member> {
        return membersRemoteDataSource.getMemberInfo(memberId)
            .map {
                it.toModel()
            }
    }

    override suspend fun getTextReviews(
        memberId: Int?,
        page: Int,
        size: Int
    ): PResult<PagingReview> {
        return membersRemoteDataSource.getTextReviews(memberId, page, size)
            .map {
                it.toModel()
            }
    }

    override suspend fun getPhotoReviews(
        memberId: Int?,
        page: Int,
        size: Int
    ): PResult<PagingReview> {
        return membersRemoteDataSource.getPhotoReviews(memberId, page, size)
            .map {
                it.toModel()
            }
    }

    override suspend fun getRecentSearchList(): StateFlow<List<String>> {
        return membersLocalDataSource.getRecentSearch()
    }

    override suspend fun deleteRecentSearch(index: Int) {
        membersLocalDataSource.deleteRecentSearch(index)
    }

    override suspend fun saveRecentSearch(search: String) {
        membersLocalDataSource.saveRecentSearch(search)
    }

    override suspend fun editProfile(request: ProfileEditRequest): PResult<Unit> {
        return membersRemoteDataSource.editProfile(request)
    }

    override suspend fun unregister(): PResult<Unit> {
        return membersRemoteDataSource.unregister()
    }

    override suspend fun findIdByEmail(request: FindByEmailRequest): PResult<UserInfo> {
        return membersRemoteDataSource.findIdByEmail(request).map {
            it.toModel()
        }
    }

    override suspend fun findIdByNickName(request: FindByNickNameRequest): PResult<UserInfo> {
        return membersRemoteDataSource.findIdByNickName(request).map {
            it.toModel()
        }
    }
}