package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.Position
import com.pinup.placePinup.domain.model.Category
import com.pinup.placePinup.domain.model.LocationBound
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.ReviewedPlace
import com.pinup.placePinup.domain.model.SortType
import com.pinup.placePinup.domain.repository.PlacesRepository


class GetReviewedPlacesUseCase (
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(locationBound: LocationBound, currentPosition: Position?, sortType: SortType, category: Category) : PResult<List<ReviewedPlace>> {
        return placesRepository.getPlaces(locationBound, currentPosition, sortType, category)
    }
}