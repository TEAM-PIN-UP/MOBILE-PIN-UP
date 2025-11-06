package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReviewsRepository

class DeleteCommentUseCase (
    private val reviewsRepository: ReviewsRepository
) {
    suspend operator fun invoke(reviewId: Int, commentId: Int): PResult<Unit> {
        return reviewsRepository.deleteComment(reviewId, commentId)
    }
}