package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.repository.ReviewsRepository

class GetFeedUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int, size: Int = 20): PResult<PagingReview> {
        return reviewsRepository.getReviews(reviewId, size)
    }
}