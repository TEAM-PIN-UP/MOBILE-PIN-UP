package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.CallRepository

class PostReadNotificationUseCase (
    private val callRepository: CallRepository,
) {
    suspend operator fun invoke(id: Int): PResult<Unit> {
        return callRepository.readNotification(id)
    }
}