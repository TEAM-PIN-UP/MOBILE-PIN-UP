package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReviewsRepository

class PostReviewLikeChangeUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int, isLike: Boolean): PResult<Unit> {
        return if (isLike) reviewsRepository.deleteReviewLike(reviewId) else reviewsRepository.addReviewLike(reviewId)
    }
}