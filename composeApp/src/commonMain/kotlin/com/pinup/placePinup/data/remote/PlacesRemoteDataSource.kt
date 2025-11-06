package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.GetReviewedPlacesRequest
import com.pinup.placePinup.data.response.GetDetailPlaceResponse
import com.pinup.placePinup.data.response.GetReviewedPlacesResponse
import com.pinup.placePinup.data.response.SearchPlacesResponse
import com.pinup.placePinup.domain.model.PResult

interface PlacesRemoteDataSource {
    suspend fun getPlaces(request: GetReviewedPlacesRequest): PResult<List<GetReviewedPlacesResponse>>
    suspend fun searchPlaces(query: String): PResult<List<SearchPlacesResponse>>
    suspend fun getDetailPlace(kakaoPlaceId: String, currentLatitude: String?, currentLongitude: String?): PResult<GetDetailPlaceResponse>
}