package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.repository.ReviewsRepository
import kotlinx.coroutines.flow.StateFlow

class GetRecentSearchUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(): StateFlow<List<String>> {
        return reviewsRepository.getRecentSearchList()
    }
}