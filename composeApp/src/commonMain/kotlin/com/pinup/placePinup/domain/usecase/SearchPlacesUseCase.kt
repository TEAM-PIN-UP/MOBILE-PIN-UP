package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.Place
import com.pinup.placePinup.domain.repository.PlacesRepository


class SearchPlacesUseCase (
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(query: String) : PResult<List<Place>> {
        return placesRepository.searchPlaces(query)
    }
}