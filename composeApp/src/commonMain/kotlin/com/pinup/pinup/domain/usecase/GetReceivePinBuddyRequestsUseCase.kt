package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingPinBuddyRequest
import com.pinup.pinup.domain.model.PinBuddyRequest
import com.pinup.pinup.domain.repository.PinBuddyRepository


class GetReceivePinBuddyRequestsUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(page: Int, size: Int): PResult<PagingPinBuddyRequest> {
        return pinBuddyRepository.getReceivedPinBuddyRequests(page, size)
    }
}