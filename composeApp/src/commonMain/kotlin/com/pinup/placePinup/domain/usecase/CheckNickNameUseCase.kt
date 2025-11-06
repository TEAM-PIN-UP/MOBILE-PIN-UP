package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.MembersRepository


class CheckNickNameUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(nickname: String): PResult<Boolean> {
       return membersRepository.checkNickName(nickname)
    }
}