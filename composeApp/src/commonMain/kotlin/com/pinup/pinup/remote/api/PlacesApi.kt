package com.pinup.pinup.remote.api

import com.pinup.pinup.data.response.GetDetailPlaceResponse
import com.pinup.pinup.data.response.GetReviewedPlacesResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.data.response.SearchPlacesResponse
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.SortType
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.parameters

interface PlacesApi {
    @GET("api/places")
    suspend fun getPlaces(
        @Query("sort") sort: SortType,
        @Query("category") category: Category,
        @Query("swLatitude") swLatitude: String,
        @Query("swLongitude") swLongitude: String,
        @Query("neLatitude") neLatitude: String,
        @Query("neLongitude") neLongitude: String,
        @Query("currentLatitude") currentLatitude: String?,
        @Query("currentLongitude") currentLongitude: String?,
    ): PResult<PResponse<List<GetReviewedPlacesResponse>>>

    @GET("api/places/keyword")
    suspend fun searchPlaces(@Query("query") query: String): PResult<PResponse<List<SearchPlacesResponse>>>

    @GET("api/places/{kakaoPlaceId}")
    suspend fun getDetailPlace(
        @Path("kakaoPlaceId") kakaoPlaceId: String,
        @Query("currentLatitude") currentLatitude: String?,
        @Query("currentLongitude") currentLongitude: String?,
    ): PResult<PResponse<GetDetailPlaceResponse>>
}