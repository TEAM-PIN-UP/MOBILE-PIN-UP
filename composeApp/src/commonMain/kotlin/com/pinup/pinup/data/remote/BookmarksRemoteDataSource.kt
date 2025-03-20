package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.AddBookmarksRequest
import com.pinup.pinup.data.response.GetBookmarksResponse
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType

interface BookmarksRemoteDataSource {
    suspend fun addBookmark(request: AddBookmarksRequest): PResult<Int>
    suspend fun deleteBookmark(kakaoPlaceId: String): PResult<Unit>
    suspend fun getBookmarks(
        category: Category,
        sort: SortType,
        currentLatitude: String,
        currentLongitude: String
    ): PResult<List<GetBookmarksResponse>>
}