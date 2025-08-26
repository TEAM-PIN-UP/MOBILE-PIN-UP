package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.model.PinlogDetail
import kotlinx.coroutines.flow.StateFlow

interface ReviewsRepository {
    suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<Int>

    suspend fun getReviews(request: Int, size: Int): PResult<PagingReview>
    suspend fun deleteReview(request: Int): PResult<Unit>
    suspend fun addReviewLike(request: Int): PResult<Unit>
    suspend fun deleteReviewLike(request: Int): PResult<Unit>

    suspend fun editReview(reviewId: Int, request: ReviewRequest): PResult<Unit>
    suspend fun getReviewDetail(request: Int): PResult<PinlogDetail>
    suspend fun addComment(reviewId: Int, request: CommentRequest): PResult<Unit>
    suspend fun deleteComment(reviewId: Int, commentId: Int): PResult<Unit>
    suspend fun editComment(reviewId: Int, commentId: Int, content: String): PResult<Unit>
    suspend fun getRecentSearchList(): StateFlow<List<String>>
    suspend fun deleteRecentSearch(index: Int)
    suspend fun saveRecentSearch(search: String)
}