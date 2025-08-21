package com.pinup.pinup.data.remote

import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult

interface ReviewsRemoteDataSource {
    suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<PResponse<String>>
}