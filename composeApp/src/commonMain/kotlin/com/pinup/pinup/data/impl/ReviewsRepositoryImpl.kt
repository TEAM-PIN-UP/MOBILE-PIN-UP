package com.pinup.pinup.data.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.domain.model.PResult
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
}