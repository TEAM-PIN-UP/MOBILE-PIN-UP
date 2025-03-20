package com.pinup.pinup.data.impl

import com.naver.maps.geometry.LatLng
import com.pinup.pinup.data.remote.PlacesRemoteDataSource
import com.pinup.pinup.data.request.GetReviewedPlacesRequest
import com.pinup.pinup.data.response.GetDetailPlaceResponse.Companion.toModel
import com.pinup.pinup.data.response.GetReviewedPlacesResponse.Companion.toModel
import com.pinup.pinup.data.response.SearchPlacesResponse.Companion.toModel
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.LocationBound
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.model.map
import com.pinup.pinup.domain.repository.PlacesRepository
import javax.inject.Inject

class PlacesRepositoryImpl (
    private val placesRemoteDataSource: PlacesRemoteDataSource,
): PlacesRepository {
    override suspend fun getPlaces(locationBound: LocationBound, currentLatLng: LatLng?, sortType: SortType, category: Category): PResult<List<ReviewedPlace>> {
        return placesRemoteDataSource.getPlaces(GetReviewedPlacesRequest.of(locationBound, currentLatLng, sortType, category))
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