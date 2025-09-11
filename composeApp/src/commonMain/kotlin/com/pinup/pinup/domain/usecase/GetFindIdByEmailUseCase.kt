package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.findId.FindByEmailRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.repository.MembersRepository

class GetFindIdByEmailUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke(request: FindByEmailRequest): PResult<UserInfo>{
        return membersRepositoryImpl.findIdByEmail(request)
    }
}