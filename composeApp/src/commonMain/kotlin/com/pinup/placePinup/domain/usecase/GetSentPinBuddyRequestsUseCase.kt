package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingPinBuddyRequest
import com.pinup.placePinup.domain.repository.PinBuddyRepository


class GetSentPinBuddyRequestsUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(page: Int, size: Int): PResult<PagingPinBuddyRequest> {
        return pinBuddyRepository.getSentPinBuddyRequests(page, size)
    }
}