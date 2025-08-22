package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.response.GetPinlogDetailResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.remote.api.ReviewsApi


class ReviewsRemoteDataSourceImpl (
    private val reviewsApi: ReviewsApi,
) : ReviewsRemoteDataSource {
    override suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<PResponse<String>> {
        return reviewsApi.registerReviews(
            request = request
        )
    }

    override suspend fun deleteReview(request: Int): PResult<PResponse<Unit>> {
        return reviewsApi.deleteReview(
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
}