package com.pinup.pinup.data.impl

import com.pinup.pinup.data.remote.BookmarksRemoteDataSource
import com.pinup.pinup.data.request.AddBookmarksRequest
import com.pinup.pinup.data.response.GetBookmarksResponse.Companion.toModel
import com.pinup.pinup.domain.model.BookmarkedPlace
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.model.map
import com.pinup.pinup.domain.repository.BookmarksRepository

class BookmarksRepositoryImpl (
    private val bookmarksRemoteDataSource: BookmarksRemoteDataSource,
) : BookmarksRepository {
    override suspend fun addBookmark(kakaoPlaceId: String): PResult<Int> {
        return bookmarksRemoteDataSource.addBookmark(
            AddBookmarksRequest(
            kakaoPlaceId = kakaoPlaceId
        ))
    }

    override suspend fun deleteBookmark(kakaoPlaceId: String): PResult<Unit> {
        return bookmarksRemoteDataSource.deleteBookmark(kakaoPlaceId)
    }

    override suspend fun getBookmarks(
        category: Category,
        sort: SortType,
        currentLatitude: String,
        currentLongitude: String
    ): PResult<List<BookmarkedPlace>> {
        return bookmarksRemoteDataSource.getBookmarks(
            category = category,
            sort = sort,
            currentLatitude = currentLatitude,
            currentLongitude = currentLongitude,
        ).map { response ->
            response.map {
                it.toModel()
            }
        }
    }
}