package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.data.response.GetPinlogDetailResponse
import com.pinup.pinup.data.response.GetReviewsResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.data.response.RegisterReviewResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingReview

interface ReviewsRemoteDataSource {
    suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<PResponse<RegisterReviewResponse>>
    suspend fun getReviews(request: Int?, size: Int, memberId: Int?, keyword: String?): PResult<PResponse<GetReviewsResponse>>
    suspend fun deleteReview(request: Int): PResult<PResponse<Unit>>
    suspend fun addReviewLike(request: Int): PResult<PResponse<Unit>>
    suspend fun deleteReviewLike(request: Int): PResult<PResponse<Unit>>

    suspend fun editReview(reviewId: Int, request: ReviewRequest): PResult<PResponse<Unit>>
    suspend fun getReviewDetail(request: Int): PResult<PResponse<GetPinlogDetailResponse>>
    suspend fun addComment(reviewId: Int, request: CommentRequest): PResult<PResponse<Unit>>
    suspend fun deleteComment(reviewId: Int, commentId: Int): PResult<PResponse<Unit>>
    suspend fun editComment(reviewId: Int, commentId: Int, content: String): PResult<PResponse<Unit>>
}