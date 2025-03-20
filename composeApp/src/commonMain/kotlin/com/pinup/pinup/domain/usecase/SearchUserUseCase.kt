package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PinBuddy
import com.pinup.pinup.domain.repository.MembersRepository
import javax.inject.Inject

class SearchUserUseCase (
    private val membersRepository: MembersRepository
) {
    suspend operator fun invoke(nickname: String) : PResult<List<PinBuddy>> {
        return membersRepository.searchUser(nickname)
    }
}