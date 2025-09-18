package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.WriteReview
import com.pinup.pinup.domain.repository.ReviewsRepository
import com.pinup.pinup.platform.hLog


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