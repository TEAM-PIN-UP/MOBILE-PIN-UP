package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.repository.MembersRepository

class SaveRecentPinBuddySearchUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(search: String) {
        return membersRepository.saveRecentSearch(search)
    }
}