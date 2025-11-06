package com.pinup.placePinup.domain.repository

import com.pinup.placePinup.domain.model.BookmarkedPlace
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.SortType

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