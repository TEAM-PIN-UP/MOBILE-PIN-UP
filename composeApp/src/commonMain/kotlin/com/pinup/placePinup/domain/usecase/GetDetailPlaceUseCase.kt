package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.DetailPlace
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.PlacesRepository


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