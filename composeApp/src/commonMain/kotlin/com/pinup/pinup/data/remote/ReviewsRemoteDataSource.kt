package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.PlaceRequest
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import java.io.File

interface ReviewsRemoteDataSource {
    suspend fun registerReviews(
        files: List<File>,
        reviewRequest: ReviewRequest,
        placeRequest: PlaceRequest,
    ): PResult<PResponse<String>>
}