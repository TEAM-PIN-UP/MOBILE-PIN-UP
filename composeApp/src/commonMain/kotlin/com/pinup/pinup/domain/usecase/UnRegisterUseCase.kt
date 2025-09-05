package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.MembersRepository

class UnRegisterUseCase (
    private val membersRepository: MembersRepository
) {
    suspend operator fun invoke() : PResult<Unit> {
        return membersRepository.unregister()
    }
}