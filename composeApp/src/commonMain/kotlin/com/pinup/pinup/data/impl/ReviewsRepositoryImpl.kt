package com.pinup.pinup.data.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.data.response.GetDetailPlaceResponse.Companion.toModel
import com.pinup.pinup.data.response.GetPinlogDetailResponse.Companion.toModel
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PinlogDetail
import com.pinup.pinup.domain.model.map
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.domain.repository.ReviewsRepository


class ReviewsRepositoryImpl (
    private val reviewsRemoteDataSource: ReviewsRemoteDataSource,
) : ReviewsRepository {
    override suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<String> {
        return reviewsRemoteDataSource.registerReviews(
            request
        ).mapSuccessData()
    }

    override suspend fun deleteReview(request: Int): PResult<Unit> {
        return reviewsRemoteDataSource.deleteReview(
            request
        ).mapSuccessData()
    }

    override suspend fun editReview(reviewId: Int, request: ReviewRequest): PResult<Unit> {
        return reviewsRemoteDataSource.editReview(
            reviewId = reviewId,
            request = request
        ).mapSuccessData()
    }

    override suspend fun getReviewDetail(request: Int): PResult<PinlogDetail> {
        return reviewsRemoteDataSource.getReviewDetail(
            request
        ).mapSuccessData().map {
            it.toModel()
        }
    }

    override suspend fun addComment(
        reviewId: Int,
        request: CommentRequest
    ): PResult<Unit> {
        return reviewsRemoteDataSource.addComment(
            reviewId = reviewId,
            request = request
        ).mapSuccessData()
    }

    override suspend fun deleteComment(
        reviewId: Int,
        commentId: Int
    ): PResult<Unit> {
        return reviewsRemoteDataSource.deleteComment(
            reviewId = reviewId,
            commentId = commentId
        ).mapSuccessData()
    }

    override suspend fun editComment(
        reviewId: Int,
        commentId: Int,
        content: String
    ): PResult<Unit> {
        return reviewsRemoteDataSource.editComment(
            reviewId = reviewId,
            commentId = commentId,
            content = content
        ).mapSuccessData()
    }
}