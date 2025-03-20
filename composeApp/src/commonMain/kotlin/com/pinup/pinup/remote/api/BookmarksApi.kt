package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.AddBookmarksRequest
import com.pinup.pinup.data.response.GetBookmarksResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface BookmarksApi {
    @POST("api/bookmarks")
    suspend fun addBookmark(@Body request: AddBookmarksRequest): PResult<PResponse<Int>>

    @DELETE("api/bookmarks/{kakaoPlaceId}")
    suspend fun deleteBookmark(@Path("kakaoPlaceId") kakaoPlaceId: String): PResult<PResponse<Unit>>

    @GET("api/bookmarks")
    suspend fun getBookmarks(
        @Query("category") category: Category,
        @Query("sort") sort: SortType,
        @Query("currentLatitude") currentLatitude: String,
        @Query("currentLongitude") currentLongitude: String,
        ): PResult<PResponse<List<GetBookmarksResponse>>>
}