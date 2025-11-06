package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.response.GetDetailPlaceResponse
import com.pinup.placePinup.data.response.GetReviewedPlacesResponse
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.data.response.SearchPlacesResponse
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.SortType
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

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