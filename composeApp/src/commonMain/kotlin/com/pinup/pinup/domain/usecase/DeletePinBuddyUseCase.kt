package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.PinBuddyRepository


class DeletePinBuddyUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(friendId: String): PResult<Unit> {
        return pinBuddyRepository.deletePinBuddy(friendId)
    }
}