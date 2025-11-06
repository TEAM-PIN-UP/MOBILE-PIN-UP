package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PinlogDetail
import com.pinup.placePinup.domain.repository.ReviewsRepository

class GetPinlogDetailUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int): PResult<PinlogDetail> {
        return reviewsRepository.getReviewDetail(reviewId)
    }
}