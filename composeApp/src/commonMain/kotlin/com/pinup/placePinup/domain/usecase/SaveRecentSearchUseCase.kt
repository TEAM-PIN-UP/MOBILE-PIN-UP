package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.repository.ReviewsRepository

class SaveRecentSearchUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(search: String) {
        return reviewsRepository.saveRecentSearch(search)
    }
}