package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PinlogDetail
import com.pinup.pinup.domain.repository.ReviewsRepository

class GetPinlogDetailUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int): PResult<PinlogDetail> {
        return reviewsRepository.getReviewDetail(reviewId)
    }
}