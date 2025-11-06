package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.ReviewRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReviewsRepository

class EditPinlogUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int, request: ReviewRequest): PResult<Unit> {
        return reviewsRepository.editReview(reviewId, request)
    }
}