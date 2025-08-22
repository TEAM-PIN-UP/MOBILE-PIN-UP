package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PinlogDetail
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.WriteReview

interface ReviewsRepository {
    suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<String>

    suspend fun deleteReview(request: Int): PResult<Unit>

    suspend fun editReview(reviewId: Int, request: ReviewRequest): PResult<Unit>
    suspend fun getReviewDetail(request: Int): PResult<PinlogDetail>
    suspend fun addComment(reviewId: Int, request: CommentRequest): PResult<Unit>
    suspend fun deleteComment(reviewId: Int, commentId: Int): PResult<Unit>
    suspend fun editComment(reviewId: Int, commentId: Int, content: String): PResult<Unit>
}