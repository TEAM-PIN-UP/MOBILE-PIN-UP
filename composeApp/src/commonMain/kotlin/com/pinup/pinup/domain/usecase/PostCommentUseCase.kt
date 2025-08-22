package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.ReviewsRepository

class PostCommentUseCase (
    private val reviewsRepository: ReviewsRepository
) {
    suspend operator fun invoke(reviewId: Int, request: CommentRequest): PResult<Unit> {
        return reviewsRepository.addComment(reviewId, request)
    }
}