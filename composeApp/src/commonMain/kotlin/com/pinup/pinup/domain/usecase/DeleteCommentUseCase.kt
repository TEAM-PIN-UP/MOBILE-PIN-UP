package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ReviewsRepository

class DeleteCommentUseCase (
    private val reviewsRepository: ReviewsRepository
) {
    suspend operator fun invoke(reviewId: Int, commentId: Int): PResult<Unit> {
        return reviewsRepository.deleteComment(reviewId, commentId)
    }
}