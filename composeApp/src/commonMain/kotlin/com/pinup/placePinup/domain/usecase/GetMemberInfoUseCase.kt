package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.Member
import com.pinup.placePinup.domain.repository.MembersRepository


class GetMemberInfoUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke(memberId: Int? = null) : PResult<Member> {
        return membersRepositoryImpl.getMemberInfo(memberId)
    }
}