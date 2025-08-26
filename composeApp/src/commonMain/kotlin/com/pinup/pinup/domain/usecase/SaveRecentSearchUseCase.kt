package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.repository.ReviewsRepository

class SaveRecentSearchUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(search: String) {
        return reviewsRepository.saveRecentSearch(search)
    }
}