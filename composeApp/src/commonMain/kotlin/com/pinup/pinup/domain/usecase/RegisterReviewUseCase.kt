package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Place
import com.pinup.pinup.domain.model.WriteReview
import com.pinup.pinup.domain.repository.ReviewsRepository
import java.io.File
import javax.inject.Inject

class RegisterReviewUseCase (
    private val reviewsRepository: ReviewsRepository,
) {
    suspend operator fun invoke(
        files: List<File>,
        writeReview: WriteReview,
        place: Place,
    ): PResult<String> {
        return reviewsRepository.registerReviews(
            files = files,
            writeReview = writeReview,
            place = place
        )
    }
}