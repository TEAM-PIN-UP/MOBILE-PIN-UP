package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.findId.FindByNickNameRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.UserInfo
import com.pinup.pinup.domain.repository.MembersRepository

class GetFindIdByNicknameUseCase (
    private val membersRepositoryImpl: MembersRepository
) {
    suspend operator fun invoke(request: FindByNickNameRequest): PResult<UserInfo>{
        return membersRepositoryImpl.findIdByNickName(request)
    }
}