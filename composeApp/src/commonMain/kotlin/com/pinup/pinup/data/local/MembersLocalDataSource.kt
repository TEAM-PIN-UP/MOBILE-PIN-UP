package com.pinup.pinup.data.local

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.UserInfo
import kotlinx.coroutines.flow.StateFlow

interface MembersLocalDataSource {
    suspend fun saveToken(tokenInfo: TokenInfo): PResult<Unit>
    suspend fun saveUserInfo(userInfo: UserInfo)
    suspend fun getAccessToken(): String
    suspend fun getRefreshToken(): String
    suspend fun getUserInfo(): StateFlow<UserInfo>
    suspend fun logout()
}