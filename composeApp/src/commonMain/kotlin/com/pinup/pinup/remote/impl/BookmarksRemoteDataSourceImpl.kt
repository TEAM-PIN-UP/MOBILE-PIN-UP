package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.BookmarksRemoteDataSource
import com.pinup.pinup.data.request.AddBookmarksRequest
import com.pinup.pinup.data.response.GetBookmarksResponse
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.BookmarksApi
import javax.inject.Inject

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