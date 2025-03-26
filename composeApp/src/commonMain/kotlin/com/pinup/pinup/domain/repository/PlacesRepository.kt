package com.pinup.pinup.domain.repository

import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.Position
import com.pinup.pinup.domain.model.LocationBound
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType

interface PlacesRepository {
    suspend fun getPlaces(locationBound: LocationBound, currentPosition: Position?, sortType: SortType, category: Category): PResult<List<ReviewedPlace>>
    suspend fun searchPlaces(query: String): PResult<List<Place>>
    suspend fun getDetailPlace(kakaoPlaceId: String, currentLatitude: String?, currentLongitude: String?): PResult<DetailPlace>
}