package com.pinup.pinup.local.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pinup.pinup.data.local.MembersLocalDataSource
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.platform.hLog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class MembersLocalDataSourceImpl (
    private val dataStore: DataStore<Preferences>
) : MembersLocalDataSource {
    private val cachedUserInfo: MutableStateFlow<UserInfo> = MutableStateFlow(UserInfo())

    override suspend fun getUserInfo(): StateFlow<UserInfo> {
        //hLog("cachedUserInfo >> ${cachedUserInfo.value}")
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
        //hLog("saveUserInfo >> ${userInfo}")
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

    private val cachedRecentSearchList = MutableStateFlow<List<String>>(emptyList())

    override suspend fun getRecentSearch(): StateFlow<List<String>> {
        ensureLoaded()
        return cachedRecentSearchList.asStateFlow()
    }

    /** 이미 있는 검색어는 제거 후 맨 앞(0)에 삽입, 최대 10개 유지 */
    override suspend fun saveRecentSearch(search: String) {
        dataStore.edit { prefs ->
            val list = readListFromPrefs(prefs).toMutableList()

            val normalized = search.trim()
            if (normalized.isEmpty()) return@edit

            // 중복은 제거 후 앞으로 이동
            list.removeAll { it == normalized }
            list.add(0, normalized)

            val trimmed = list.take(MAX_RECENT)
            writeListToPrefs(prefs, trimmed)
            cachedRecentSearchList.value = trimmed
        }
    }

    /** 전달된 index의 항목만 제거 */
    override suspend fun deleteRecentSearch(index: Int) {
        dataStore.edit { prefs ->
            val list = readListFromPrefs(prefs).toMutableList()
            if (index in list.indices) {
                list.removeAt(index)
                writeListToPrefs(prefs, list)
                cachedRecentSearchList.value = list
            }
        }
    }

    // --- 내부 유틸 ---

    private suspend fun ensureLoaded() {
        if (cachedRecentSearchList.value.isNotEmpty()) return
        val prefs = dataStore.data.first()
        cachedRecentSearchList.value = readListFromPrefs(prefs)
    }

    private fun readListFromPrefs(prefs: Preferences): List<String> {
        val result = ArrayList<String>(MAX_RECENT)
        for (i in 0 until MAX_RECENT) {
            val key = stringPreferencesKey("$KEY_PREFIX$i")
            val value = prefs[key]
            if (!value.isNullOrEmpty()) result.add(value)
        }
        return result
    }

    private fun writeListToPrefs(prefs: MutablePreferences, list: List<String>) {
        for (i in 0 until MAX_RECENT) {
            val key = stringPreferencesKey("$KEY_PREFIX$i")
            if (i < list.size) {
                prefs[key] = list[i]        // 값 설정
            } else {
                prefs.remove(key)           // 남는 키는 제거
            }
        }
    }

    companion object {
        private const val KEY_ACCESS_TOKEN = "key_pinup_access_token"
        private const val KEY_REFRESH_TOKEN = "key_pinup_refresh_token"
        private const val KEY_USER_INFO = "key_pinup_user_info"
        private const val KEY_PREFIX = "recent_search_pin_buddy"
        private const val MAX_RECENT = 10
    }
}