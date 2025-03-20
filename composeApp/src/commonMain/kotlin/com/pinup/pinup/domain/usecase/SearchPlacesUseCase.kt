package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.repository.PlacesRepository
import javax.inject.Inject

class SearchPlacesUseCase (
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(query: String) : PResult<List<Place>> {
        return placesRepository.searchPlaces(query)
    }
}