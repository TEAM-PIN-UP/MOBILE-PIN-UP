package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.UserInfo
import com.pinup.placePinup.domain.repository.MembersRepository

class GetFindIdByEmailUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke(request: String): PResult<UserInfo>{
        return membersRepositoryImpl.findIdByEmail(request)
    }
}