package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.repository.MembersRepository
import kotlinx.coroutines.flow.StateFlow

class GetRecentPinBuddySearchUseCase (
    private val membersRepository: MembersRepository,
) {
    suspend operator fun invoke(): StateFlow<List<String>> {
        return membersRepository.getRecentSearchList()
    }
}