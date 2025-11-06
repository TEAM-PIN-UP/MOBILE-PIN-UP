package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.review.CommentRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReviewsRepository

class PostCommentUseCase (
    private val reviewsRepository: ReviewsRepository
) {
    suspend operator fun invoke(reviewId: Int, request: CommentRequest): PResult<Unit> {
        return reviewsRepository.addComment(reviewId, request)
    }
}