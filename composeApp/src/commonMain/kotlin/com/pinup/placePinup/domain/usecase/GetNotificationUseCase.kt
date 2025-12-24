package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PagingNotification
import com.pinup.placePinup.domain.repository.CallRepository

class GetNotificationUseCase (
    private val callRepository: CallRepository
) {
    suspend operator fun invoke(page: Int): PResult<PagingNotification> {
        return callRepository.getMyNotification(page)
    }
}