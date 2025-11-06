package com.pinup.placePinup.domain.repository

import com.pinup.placePinup.data.request.ReviewRequest
import com.pinup.placePinup.data.request.pinlog.AddReviewRequest
import com.pinup.placePinup.data.request.review.CommentRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingReview
import com.pinup.placePinup.domain.model.PinlogDetail
import kotlinx.coroutines.flow.StateFlow

interface ReviewsRepository {
    suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<Int>

    suspend fun getReviews(request: Int?, size: Int, memberId: Int?, keyword: String?): PResult<PagingReview>
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