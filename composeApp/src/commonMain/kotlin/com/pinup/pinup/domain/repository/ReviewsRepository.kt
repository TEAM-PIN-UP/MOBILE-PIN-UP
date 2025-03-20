package com.pinup.pinup.domain.repository

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.WriteReview
import java.io.File

interface ReviewsRepository {
    suspend fun registerReviews(
        files: List<File>,
        writeReview: WriteReview,
        place: Place,
    ): PResult<String>
}