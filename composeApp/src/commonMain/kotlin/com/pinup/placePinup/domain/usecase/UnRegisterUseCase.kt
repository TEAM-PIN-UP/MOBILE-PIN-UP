package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.MembersRepository

class UnRegisterUseCase (
    private val membersRepository: MembersRepository
) {
    suspend operator fun invoke() : PResult<Unit> {
        return membersRepository.unregister()
    }
}