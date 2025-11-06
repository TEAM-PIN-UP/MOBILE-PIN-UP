package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingPinBuddy
import com.pinup.placePinup.domain.repository.PinBuddyRepository


class GetPinBuddiesUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(page: Int, size: Int): PResult<PagingPinBuddy> {
        return pinBuddyRepository.getPinBuddies(page, size)
    }
}