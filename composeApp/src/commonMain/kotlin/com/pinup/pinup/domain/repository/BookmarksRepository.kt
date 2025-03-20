package com.pinup.pinup.domain.repository

import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType

interface BookmarksRepository {
    suspend fun addBookmark(kakaoPlaceId: String): PResult<Int>
    suspend fun deleteBookmark(kakaoPlaceId: String): PResult<Unit>
    suspend fun getBookmarks(
        category: Category,
        sort: SortType,
        currentLatitude: String,
        currentLongitude: String
    ): PResult<List<BookmarkedPlace>>
}