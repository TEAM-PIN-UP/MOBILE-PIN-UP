package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.pinlog.AddReviewRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReviewsRepository
import com.pinup.placePinup.platform.hLog


class RegisterReviewUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(
        request: AddReviewRequest,
    ): PResult<Int> {
        hLog(request.toString())
        return reviewsRepository.registerReviews(request)
    }
}