package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.CallRepository

class PostAllReadNotificationUseCase (
    private val callRepository: CallRepository,
) {
    suspend operator fun invoke(): PResult<Unit> {
        return callRepository.readAllNotification()
    }
}