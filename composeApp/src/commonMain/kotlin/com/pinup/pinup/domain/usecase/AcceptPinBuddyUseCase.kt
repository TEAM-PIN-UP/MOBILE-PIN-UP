package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.PinBuddyRepository
import javax.inject.Inject

class AcceptPinBuddyUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(friendRequestId: Int): PResult<Unit> {
        return pinBuddyRepository.acceptPinBuddy(friendRequestId)
    }
}