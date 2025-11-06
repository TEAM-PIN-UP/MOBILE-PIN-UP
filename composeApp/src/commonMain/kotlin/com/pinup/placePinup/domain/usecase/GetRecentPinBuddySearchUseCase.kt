package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.repository.MembersRepository
import kotlinx.coroutines.flow.StateFlow

class GetRecentPinBuddySearchUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(): StateFlow<List<String>> {
        return membersRepository.getRecentSearchList()
    }
}