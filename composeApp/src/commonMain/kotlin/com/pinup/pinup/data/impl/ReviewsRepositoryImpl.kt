package com.pinup.pinup.data.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
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
}