package com.pinup.placePinup.data.impl

import com.pinup.placePinup.data.remote.PlacesRemoteDataSource
import com.pinup.placePinup.data.request.GetReviewedPlacesRequest
import com.pinup.placePinup.data.response.GetDetailPlaceResponse.Companion.toModel
import com.pinup.placePinup.data.response.GetReviewedPlacesResponse.Companion.toModel
import com.pinup.placePinup.data.response.SearchPlacesResponse.Companion.toModel
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.DetailPlace
import com.pinup.placePinup.domain.model.LocationBound
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.model.ReviewedPlace
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.model.map
import com.pinup.placePinup.domain.repository.PlacesRepository
import com.pinup.placePinup.domain.model.Position


class PlacesRepositoryImpl (
    private val placesRemoteDataSource: PlacesRemoteDataSource,
): PlacesRepository {
    override suspend fun getPlaces(locationBound: LocationBound, currentPosition: Position?, sortType: SortType, category: Category): PResult<List<ReviewedPlace>> {
        return placesRemoteDataSource.getPlaces(GetReviewedPlacesRequest.of(locationBound, currentPosition, sortType, category))
            .map {
                it.map { response ->
                    response.toModel()
                }
            }
    }

    override suspend fun searchPlaces(query: String): PResult<List<Place>> {
        return placesRemoteDataSource.searchPlaces(query)
            .map {
                it.map { response ->
                    response.toModel()
                }
            }
    }

    override suspend fun getDetailPlace(
        kakaoPlaceId: String,
        currentLatitude: String?,
        currentLongitude: String?
    ): PResult<DetailPlace> {
        return placesRemoteDataSource.getDetailPlace(
            kakaoPlaceId = kakaoPlaceId,
            currentLatitude = currentLatitude,
            currentLongitude = currentLongitude,
        ).map {
            it.toModel()
        }
    }
}