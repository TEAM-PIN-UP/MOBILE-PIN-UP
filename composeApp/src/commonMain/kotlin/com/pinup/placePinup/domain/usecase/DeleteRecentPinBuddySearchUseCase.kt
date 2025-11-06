package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.repository.MembersRepository

class DeleteRecentPinBuddySearchUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(index: Int) {
        return membersRepository.deleteRecentSearch(index)
    }
}