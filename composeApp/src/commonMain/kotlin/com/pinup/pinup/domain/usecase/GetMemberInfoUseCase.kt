package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.Member
import com.pinup.pinup.domain.repository.MembersRepository
import javax.inject.Inject

class GetMemberInfoUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke(memberId: Int? = null) : PResult<Member> {
        return membersRepositoryImpl.getMemberInfo(memberId)
    }
}