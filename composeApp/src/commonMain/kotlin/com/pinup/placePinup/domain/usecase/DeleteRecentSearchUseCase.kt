package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.repository.ReviewsRepository

class DeleteRecentSearchUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(index: Int) {
        return reviewsRepository.deleteRecentSearch(index)
    }
}