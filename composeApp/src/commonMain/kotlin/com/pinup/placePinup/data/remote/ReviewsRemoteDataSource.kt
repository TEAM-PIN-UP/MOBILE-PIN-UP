package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.ReviewRequest
import com.pinup.placePinup.data.request.pinlog.AddReviewRequest
import com.pinup.placePinup.data.request.review.CommentRequest
import com.pinup.placePinup.data.response.GetPinlogDetailResponse
import com.pinup.placePinup.data.response.GetReviewsResponse
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.data.response.RegisterReviewResponse
import com.pinup.placePinup.domain.model.PResult

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