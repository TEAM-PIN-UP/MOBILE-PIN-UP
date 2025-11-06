package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PinBuddy
import com.pinup.placePinup.domain.repository.MembersRepository


class SearchUserUseCase (
    private val membersRepository: MembersRepository
) {
    suspend operator fun invoke(nickname: String) : PResult<List<PinBuddy>> {
        return membersRepository.searchUser(nickname)
    }
}