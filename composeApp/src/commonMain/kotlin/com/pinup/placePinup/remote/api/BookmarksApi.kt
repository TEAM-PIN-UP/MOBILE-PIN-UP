package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.request.AddBookmarksRequest
import com.pinup.placePinup.data.response.GetBookmarksResponse
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.SortType
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

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