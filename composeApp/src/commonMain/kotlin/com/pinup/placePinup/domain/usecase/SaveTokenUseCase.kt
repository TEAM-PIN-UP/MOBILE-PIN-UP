package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.TokenInfo
import com.pinup.placePinup.domain.repository.MembersRepository


class SaveTokenUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(tokenInfo: TokenInfo) : PResult<Unit> {
        return membersRepository.saveToken(tokenInfo)
    }
}