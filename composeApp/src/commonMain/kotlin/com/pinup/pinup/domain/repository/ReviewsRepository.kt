package com.pinup.pinup.domain.repository

import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.WriteReview

interface ReviewsRepository {
    suspend fun registerReviews(
        request: AddReviewRequest
    ): PResult<String>
}