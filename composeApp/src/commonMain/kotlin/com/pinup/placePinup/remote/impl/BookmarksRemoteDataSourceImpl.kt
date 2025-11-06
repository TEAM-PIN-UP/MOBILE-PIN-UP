package com.pinup.placePinup.remote.impl

import com.pinup.placePinup.data.remote.BookmarksRemoteDataSource
import com.pinup.placePinup.data.request.AddBookmarksRequest
import com.pinup.placePinup.data.response.GetBookmarksResponse
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.remote.api.BookmarksApi


class BookmarksRemoteDataSourceImpl (
    private val bookmarksApi: BookmarksApi,
) : BookmarksRemoteDataSource {
    override suspend fun addBookmark(request: AddBookmarksRequest): PResult<Int> {
        return bookmarksApi.addBookmark(request).mapSuccessData()
    }

    override suspend fun deleteBookmark(kakaoPlaceId: String): PResult<Unit> {
        return bookmarksApi.deleteBookmark(kakaoPlaceId).mapSuccessData()
    }

    override suspend fun getBookmarks(
        category: Category,
        sort: SortType,
        currentLatitude: String,
        currentLongitude: String
    ): PResult<List<GetBookmarksResponse>> {
        return bookmarksApi.getBookmarks(
            category = category,
            sort = sort,
            currentLongitude = currentLongitude,
            currentLatitude = currentLatitude
        ).mapSuccessData()
    }
}