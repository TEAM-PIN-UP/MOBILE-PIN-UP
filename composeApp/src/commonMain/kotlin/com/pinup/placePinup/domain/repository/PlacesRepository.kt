package com.pinup.placePinup.domain.repository

import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.DetailPlace
import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.domain.model.LocationBound
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.model.ReviewedPlace
import com.pinup.placePinup.domain.model.SortType

interface PlacesRepository {
    suspend fun getPlaces(locationBound: LocationBound, currentPosition: Position?, sortType: SortType, category: Category): PResult<List<ReviewedPlace>>
    suspend fun searchPlaces(query: String): PResult<List<Place>>
    suspend fun getDetailPlace(kakaoPlaceId: String, currentLatitude: String?, currentLongitude: String?): PResult<DetailPlace>
}