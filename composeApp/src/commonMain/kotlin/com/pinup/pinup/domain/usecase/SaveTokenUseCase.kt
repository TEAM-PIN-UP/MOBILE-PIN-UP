package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import com.pinup.pinup.domain.repository.MembersRepository


class SaveTokenUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(tokenInfo: TokenInfo) : PResult<Unit> {
        return membersRepository.saveToken(tokenInfo)
    }
}