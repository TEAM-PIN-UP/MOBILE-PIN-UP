package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.request.ProfileEditRequest
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface MembersRepository {
    suspend fun checkNickName(nickname: String): PResult<Boolean>
    suspend fun saveToken(tokenInfo: TokenInfo): PResult<Unit>
    suspend fun getUserInfo(): StateFlow<UserInfo>
    suspend fun getIsUsedApp(): Boolean
    suspend fun getAccessToken(): String
    suspend fun clearUserData()
    suspend fun saveUserInfo(userInfo: UserInfo)
    suspend fun searchUser(nickname: String): PResult<List<PinBuddy>>
    suspend fun getMemberInfo(memberId: Int?): PResult<Member>
    suspend fun getTextReviews(memberId: Int?, page: Int, size: Int): PResult<PagingReview>
    suspend fun getPhotoReviews(memberId: Int?, page: Int, size: Int): PResult<PagingReview>
    suspend fun getRecentSearchList(): StateFlow<List<String>>
    suspend fun deleteRecentSearch(index: Int)
    suspend fun saveRecentSearch(search: String)
    suspend fun editProfile(request: ProfileEditRequest): PResult<Unit>
    suspend fun unregister(): PResult<Unit>
    suspend fun findIdByEmail(request: String): PResult<UserInfo>
    suspend fun findIdByNickName(request: String): PResult<UserInfo>
}