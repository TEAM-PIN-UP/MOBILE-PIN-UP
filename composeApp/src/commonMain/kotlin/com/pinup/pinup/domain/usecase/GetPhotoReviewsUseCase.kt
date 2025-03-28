package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.repository.MembersRepository


class GetPhotoReviewsUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke(memberId: Int? = null, page: Int, size: Int) : PResult<PagingReview> {
        return membersRepositoryImpl.getPhotoReviews(memberId, page, size)
    }
}