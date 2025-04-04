package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.AddBookmarksRequest
import com.pinup.pinup.data.response.GetBookmarksResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.parameters
import io.ktor.http.path

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