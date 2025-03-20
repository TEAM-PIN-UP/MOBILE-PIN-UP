package com.pinup.pinup.data.impl

import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.data.remote.MembersRemoteDataSource
import com.pinup.pinup.data.response.GetMemberInfoResponse
import com.pinup.pinup.data.response.GetMemberInfoResponse.Companion.toModel
import com.pinup.pinup.data.response.GetReviewsResponse.Companion.toModel
import com.pinup.pinup.data.response.SearchUserResponse.Companion.toModel
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.model.map
import com.pinup.pinup.domain.repository.MembersRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class MembersRepositoryImpl (
    private val membersRemoteDataSource: MembersRemoteDataSource,
    private val membersLocalDataSource: MembersLocalDataSource
): MembersRepository {
    override suspend fun checkNickName(nickname: String): PResult<Boolean> {
        return membersRemoteDataSource.checkNickName(nickname)
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
}