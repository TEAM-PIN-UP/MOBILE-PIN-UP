package com.pinup.pinup.domain.usecase

import com.naver.maps.geometry.LatLng
import com.pinup.pinup.domain.model.Category
import com.pinup.pinup.domain.model.LocationBound
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.ReviewedPlace
import com.pinup.pinup.domain.model.SortType
import com.pinup.pinup.domain.repository.PlacesRepository
import javax.inject.Inject

class GetReviewedPlacesUseCase (
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(locationBound: LocationBound, currentLatLng: LatLng?, sortType: SortType, category: Category) : PResult<List<ReviewedPlace>> {
        return placesRepository.getPlaces(locationBound, currentLatLng, sortType, category)
    }
}