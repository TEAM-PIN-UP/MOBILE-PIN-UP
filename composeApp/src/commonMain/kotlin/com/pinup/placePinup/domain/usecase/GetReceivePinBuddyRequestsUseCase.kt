package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingPinBuddyRequest
import com.pinup.placePinup.domain.repository.PinBuddyRepository


class GetReceivePinBuddyRequestsUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(page: Int, size: Int): PResult<PagingPinBuddyRequest> {
        return pinBuddyRepository.getReceivedPinBuddyRequests(page, size)
    }
}