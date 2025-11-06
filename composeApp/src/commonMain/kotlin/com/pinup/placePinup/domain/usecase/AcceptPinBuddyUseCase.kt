package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.PinBuddyRepository


class AcceptPinBuddyUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(friendRequestId: Int): PResult<Unit> {
        return pinBuddyRepository.acceptPinBuddy(friendRequestId)
    }
}