package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReviewsRepository

class EditCommentUseCase (
    private val reviewsRepository: ReviewsRepository
) {
    suspend operator fun invoke(reviewId: Int, commentId: Int, content: String): PResult<Unit> {
        return reviewsRepository.editComment(reviewId, commentId = commentId, content)
    }
}