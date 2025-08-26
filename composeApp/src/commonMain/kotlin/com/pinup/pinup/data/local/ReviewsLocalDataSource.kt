package com.pinup.pinup.data.local

import kotlinx.coroutines.flow.StateFlow

interface ReviewsLocalDataSource {
    suspend fun deleteRecentSearch(index: Int)
    suspend fun getRecentSearch(): StateFlow<List<String>>
    suspend fun saveRecentSearch(search: String)
}