package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ReviewsRepository

class DeletePinlogUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int): PResult<Unit> {
        return reviewsRepository.deleteReview(reviewId)
    }
}