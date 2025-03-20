package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.DetailPlace
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.PlacesRepository
import javax.inject.Inject

class GetDetailPlaceUseCase (
    private val placesRepository: PlacesRepository,
) {
    suspend operator fun invoke(
        kakaoPlaceId: String,
        currentLatitude: String?,
        currentLongitude: String?
    ): PResult<DetailPlace> {
        return placesRepository.getDetailPlace(
            kakaoPlaceId = kakaoPlaceId,
            currentLatitude = currentLatitude,
            currentLongitude = currentLongitude,
        )
    }
}