package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PagingPinBuddy
import com.pinup.pinup.domain.model.Profile
import com.pinup.pinup.domain.repository.PinBuddyRepository
import javax.inject.Inject

class GetPinBuddiesUseCase (
    private val pinBuddyRepository: PinBuddyRepository,
) {
    suspend operator fun invoke(page: Int, size: Int): PResult<PagingPinBuddy> {
        return pinBuddyRepository.getPinBuddies(page, size)
    }
}