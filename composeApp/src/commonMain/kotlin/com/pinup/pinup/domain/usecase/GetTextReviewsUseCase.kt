package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.response.GetMemberInfoResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.model.PagingReview
import com.pinup.pinup.domain.repository.MembersRepository


class GetTextReviewsUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke(memberId: Int? = null, page: Int, size: Int) : PResult<PagingReview> {
        return membersRepositoryImpl.getTextReviews(memberId, page, size)
    }
}