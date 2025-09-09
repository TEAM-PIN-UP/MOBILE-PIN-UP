package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.request.review.CommentEditRequest
import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.data.response.GetPinlogDetailResponse
import com.pinup.pinup.data.response.GetReviewsResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.data.response.RegisterReviewResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.remote.api.ReviewsApi


class ReviewsRemoteDataSourceImpl (
    private val reviewsApi: ReviewsApi,
) : ReviewsRemoteDataSource {
    override suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<PResponse<RegisterReviewResponse>> {
        return reviewsApi.registerReviews(
            request = request
        )
    }

    override suspend fun getReviews(request: Int, size: Int, memberId: Int?, keyword: String?): PResult<PResponse<GetReviewsResponse>> {
        return reviewsApi.getReviews(
            cursor = request,
            size = size,
            searchMemberId = memberId,
            keyword = keyword
        )
    }

    override suspend fun deleteReview(request: Int): PResult<PResponse<Unit>> {
        return reviewsApi.deleteReview(
            reviewId = request
        )
    }

    override suspend fun addReviewLike(request: Int): PResult<PResponse<Unit>> {
        return reviewsApi.addReviewLike(
            reviewId = request
        )
    }

    override suspend fun deleteReviewLike(request: Int): PResult<PResponse<Unit>> {
        return reviewsApi.deleteReviewLike(
            reviewId = request
        )
    }

    override suspend fun editReview(reviewId: Int, request: ReviewRequest): PResult<PResponse<Unit>> {
        return reviewsApi.editReview(
            reviewId = reviewId,
            request = request
        )
    }

    override suspend fun getReviewDetail(request: Int): PResult<PResponse<GetPinlogDetailResponse>> {
        return reviewsApi.getReviewDetail(
            reviewId = request
        )
    }

    override suspend fun addComment(
        reviewId: Int,
        request: CommentRequest
    ): PResult<PResponse<Unit>> {
        return reviewsApi.addComment(
            reviewId = reviewId,
            request = request
        )
    }

    override suspend fun deleteComment(
        reviewId: Int,
        commentId: Int
    ): PResult<PResponse<Unit>> {
        return reviewsApi.deleteReview(
            reviewId = reviewId,
            commentId = commentId
        )
    }

    override suspend fun editComment(
        reviewId: Int,
        commentId: Int,
        content: String
    ): PResult<PResponse<Unit>> {
        return reviewsApi.editReview(
            reviewId = reviewId,
            commentId = commentId,
            request = CommentEditRequest(content)
        )
    }
}