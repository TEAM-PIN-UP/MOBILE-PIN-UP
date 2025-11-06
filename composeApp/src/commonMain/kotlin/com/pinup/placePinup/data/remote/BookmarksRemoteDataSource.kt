package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.AddBookmarksRequest
import com.pinup.placePinup.data.response.GetBookmarksResponse
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.SortType

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