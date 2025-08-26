package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.repository.ReviewsRepository
import kotlinx.coroutines.flow.StateFlow

class GetRecentSearchUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(): StateFlow<List<String>> {
        return reviewsRepository.getRecentSearchList()
    }
}