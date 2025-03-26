package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.data.request.PlaceRequest
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.remote.api.ReviewsApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ReviewsRemoteDataSourceImpl (
    private val reviewsApi: ReviewsApi,
) : ReviewsRemoteDataSource {
    override suspend fun registerReviews(
        files: List<ByteArray>,
        reviewRequest: ReviewRequest,
        placeRequest: PlaceRequest
    ): PResult<PResponse<String>> {
        val reviewRequestBody = Json.encodeToString(reviewRequest)
        val placeRequestBody = Json.encodeToString(placeRequest)
        return reviewsApi.registerReviews(
            files = files,
            reviewRequest = reviewRequestBody,
            placeRequest = placeRequestBody
        )
    }
}