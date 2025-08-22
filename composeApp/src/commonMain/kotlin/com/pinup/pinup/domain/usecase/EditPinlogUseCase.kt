package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ReviewsRepository

class EditPinlogUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int, request: ReviewRequest): PResult<Unit> {
        return reviewsRepository.editReview(reviewId, request)
    }
}