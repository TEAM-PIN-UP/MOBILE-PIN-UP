package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingReview
import com.pinup.placePinup.domain.repository.ReviewsRepository

class GetFeedUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(reviewId: Int?, size: Int = 20, memberId: Int?, keyword: String?): PResult<PagingReview> {
        return reviewsRepository.getReviews(reviewId, size, memberId, keyword)
    }
}