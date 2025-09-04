package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.repository.MembersRepository

class DeleteRecentPinBuddySearchUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(index: Int) {
        return membersRepository.deleteRecentSearch(index)
    }
}