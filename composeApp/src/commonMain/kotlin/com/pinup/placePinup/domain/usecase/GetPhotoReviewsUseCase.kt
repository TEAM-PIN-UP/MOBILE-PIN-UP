package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingReview
import com.pinup.placePinup.domain.repository.MembersRepository


class GetPhotoReviewsUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke(memberId: Int? = null, page: Int, size: Int) : PResult<PagingReview> {
        return membersRepositoryImpl.getPhotoReviews(memberId, page, size)
    }
}