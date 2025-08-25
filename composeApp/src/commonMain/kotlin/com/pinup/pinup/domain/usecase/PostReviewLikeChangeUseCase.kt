package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ReviewsRepository

class PostReviewLikeChangeUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int, isLike: Boolean): PResult<Unit> {
        return if (isLike) reviewsRepository.deleteReviewLike(reviewId) else reviewsRepository.addReviewLike(reviewId)
    }
}