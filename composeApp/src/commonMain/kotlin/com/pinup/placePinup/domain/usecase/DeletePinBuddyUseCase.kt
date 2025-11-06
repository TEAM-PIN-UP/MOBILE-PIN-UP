package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.PinBuddyRepository


class DeletePinBuddyUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(friendId: String): PResult<Unit> {
        return pinBuddyRepository.deletePinBuddy(friendId)
    }
}