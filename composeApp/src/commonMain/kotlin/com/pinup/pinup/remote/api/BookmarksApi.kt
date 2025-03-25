package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.AddBookmarksRequest
import com.pinup.pinup.data.response.GetBookmarksResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.parameters
import io.ktor.http.path

class BookmarksApi(
    private val httpClient: HttpClient
) {
    suspend fun addBookmark(request: AddBookmarksRequest): PResult<PResponse<Int>> {
        val response = httpClient.post("api/bookmarks") {
            setBody(request)
        }
        return response.body()
    }

    suspend fun deleteBookmark(kakaoPlaceId: String): PResult<PResponse<Unit>> {
        val response = httpClient.delete("api/bookmarks/$kakaoPlaceId")
        return response.body()
    }

    suspend fun getBookmarks(
        category: Category,
        sort: SortType,
        currentLatitude: String,
        currentLongitude: String,
    ): PResult<PResponse<List<GetBookmarksResponse>>> {
        val response = httpClient.get("api/bookmarks") {
            parameters {
                append("category", category.toString())
                append("sort", sort.toString())
                append("currentLatitude", currentLatitude)
                append("currentLongitude", currentLongitude)
            }
        }
        return response.body()
    }
}