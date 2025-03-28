package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.PlacesRemoteDataSource
import com.pinup.pinup.data.request.GetReviewedPlacesRequest
import com.pinup.pinup.data.response.GetDetailPlaceResponse
import com.pinup.pinup.data.response.GetReviewedPlacesResponse
import com.pinup.pinup.data.response.SearchPlacesResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.remote.api.PlacesApi


class PlacesRemoteDataSourceImpl (
    private val placesApi: PlacesApi
): PlacesRemoteDataSource {
    override suspend fun getPlaces(request: GetReviewedPlacesRequest): PResult<List<GetReviewedPlacesResponse>> {
        return placesApi.getPlaces(
            sort = request.sort,
            category = request.category,
            swLatitude = request.swLatitude,
            swLongitude = request.swLongitude,
            neLatitude = request.neLatitude,
            neLongitude = request.neLongitude,
            currentLongitude = request.currentLongitude,
            currentLatitude = request.currentLatitude
        ).mapSuccessData()
    }

    override suspend fun searchPlaces(query: String): PResult<List<SearchPlacesResponse>> {
        return placesApi.searchPlaces(query).mapSuccessData()
    }

    override suspend fun getDetailPlace(
        kakaoPlaceId: String,
        currentLatitude: String?,
        currentLongitude: String?
    ): PResult<GetDetailPlaceResponse> {
        return placesApi.getDetailPlace(
            kakaoPlaceId = kakaoPlaceId,
            currentLongitude = currentLongitude,
            currentLatitude = currentLatitude,
        ).mapSuccessData()
    }
}