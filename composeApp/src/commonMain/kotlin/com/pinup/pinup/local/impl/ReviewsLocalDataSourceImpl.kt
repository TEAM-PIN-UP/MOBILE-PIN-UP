package com.pinup.pinup.local.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pinup.pinup.data.local.ReviewsLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first

class ReviewsLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : ReviewsLocalDataSource {

    private val cachedRecentSearchList = MutableStateFlow<List<String>>(emptyList())

    override suspend fun getRecentSearch(): StateFlow<List<String>> {
        ensureLoaded()
        return cachedRecentSearchList.asStateFlow()
    }

    override suspend fun saveRecentSearch(search: String) {
        dataStore.edit { prefs ->
            val list = readListFromPrefs(prefs).toMutableList()

            // 공백만 있는 입력은 무시(원치 않으면 제거)
            val normalized = search.trim()
            if (normalized.isEmpty()) return@edit

            // 이미 존재하면 제거해서 앞으로 보냄
            list.removeAll { it == normalized }
            list.add(0, normalized)

            val trimmed = list.take(MAX_RECENT)
            writeListToPrefs(prefs, trimmed)
            cachedRecentSearchList.value = trimmed
        }
    }

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

    private suspend fun writeListToPrefs(
        prefs: MutablePreferences,
        list: List<String>
    ) {
        for (i in 0 until MAX_RECENT) {
            val key = stringPreferencesKey("$KEY_PREFIX$i")
            if (i < list.size) {
                (prefs as MutableMap<Preferences.Key<String>, String>)[key] = list[i]
            } else {
                (prefs as MutableMap<Preferences.Key<String>, String?>).remove(key)
            }
        }
    }

    companion object {
        private const val KEY_PREFIX = "recent_search_"
        private const val MAX_RECENT = 10
    }
}
