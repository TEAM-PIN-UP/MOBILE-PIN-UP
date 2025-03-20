package com.pinup.pinup.data.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.data.request.PlaceRequest
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.WriteReview
import com.pinup.pinup.domain.model.mapSuccessData
import com.pinup.pinup.domain.repository.ReviewsRepository
import java.io.File
import javax.inject.Inject

class ReviewsRepositoryImpl (
    private val reviewsRemoteDataSource: ReviewsRemoteDataSource,
) : ReviewsRepository {
    override suspend fun registerReviews(
        files: List<File>,
        writeReview: WriteReview,
        place: Place
    ): PResult<String> {
        return reviewsRemoteDataSource.registerReviews(
            files = files,
            reviewRequest = ReviewRequest.of(writeReview),
            placeRequest = PlaceRequest.of(place)
        ).mapSuccessData()
    }
}