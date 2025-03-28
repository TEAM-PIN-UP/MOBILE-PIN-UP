package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.GetReviewedPlacesRequest
import com.pinup.pinup.data.response.GetDetailPlaceResponse
import com.pinup.pinup.data.response.GetReviewedPlacesResponse
import com.pinup.pinup.data.response.SearchPlacesResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place

interface PlacesRemoteDataSource {
    suspend fun getPlaces(request: GetReviewedPlacesRequest): PResult<List<GetReviewedPlacesResponse>>
    suspend fun searchPlaces(query: String): PResult<List<SearchPlacesResponse>>
    suspend fun getDetailPlace(kakaoPlaceId: String, currentLatitude: String?, currentLongitude: String?): PResult<GetDetailPlaceResponse>
}