package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.PinBuddyRepository


class RequestPinBuddyUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(receiverId: Int): PResult<Unit> {
        return pinBuddyRepository.requestPinBuddy(receiverId)
    }
}