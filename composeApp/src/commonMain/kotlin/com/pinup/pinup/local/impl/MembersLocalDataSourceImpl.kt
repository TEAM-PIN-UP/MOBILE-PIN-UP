package com.pinup.pinup.local.impl

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MembersLocalDataSourceImpl (
    private val dataStore: DataStore<Preferences>
) : MembersLocalDataSource {
    private val cachedUserInfo: MutableStateFlow<UserInfo> = MutableStateFlow(UserInfo())

    override suspend fun getUserInfo(): StateFlow<UserInfo> {
        hLog("cachedUserInfo >> ${cachedUserInfo.value}")
        return cachedUserInfo.asStateFlow()
    }

    override suspend fun logout() {
        cachedUserInfo.value = UserInfo()
        dataStore.edit {
            it.remove(stringPreferencesKey(KEY_ACCESS_TOKEN))
            it.remove(stringPreferencesKey(KEY_REFRESH_TOKEN))
        }
    }

    override suspend fun saveUserInfo(userInfo: UserInfo) {
        hLog("saveUserInfo >> ${userInfo}")
        cachedUserInfo.value = userInfo
    }

    override suspend fun saveToken(tokenInfo: TokenInfo): PResult<Unit> {
        dataStore.edit {
            it[stringPreferencesKey(KEY_ACCESS_TOKEN)] = tokenInfo.accessToken
            it[stringPreferencesKey(KEY_REFRESH_TOKEN)] = tokenInfo.refreshToken
        }

        return PResult.Success(Unit)
    }

    override suspend fun getAccessToken(): String {
        return dataStore.data.map {
            it[stringPreferencesKey(KEY_ACCESS_TOKEN)] ?: ""
        }.first()
    }

    override suspend fun getRefreshToken(): String {
        return dataStore.data.map {
            it[stringPreferencesKey(KEY_REFRESH_TOKEN)] ?: ""
        }.first()
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "key_pinup_access_token"
        private const val KEY_REFRESH_TOKEN = "key_pinup_refresh_token"
        private const val KEY_USER_INFO = "key_pinup_user_info"
    }
}