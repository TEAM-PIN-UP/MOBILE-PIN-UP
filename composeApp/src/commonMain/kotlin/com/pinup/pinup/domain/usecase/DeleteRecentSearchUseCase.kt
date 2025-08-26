package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.repository.ReviewsRepository
import kotlinx.coroutines.flow.StateFlow

class DeleteRecentSearchUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(index: Int) {
        return reviewsRepository.deleteRecentSearch(index)
    }
}