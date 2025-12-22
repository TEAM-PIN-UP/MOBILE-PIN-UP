package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.fcm.SetDeviceTokenRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.CallRepository

class PostSetDeviceTokenUseCase (
    private val callRepository: CallRepository
) {
    suspend operator fun invoke(request: SetDeviceTokenRequest): PResult<Unit> {
        return callRepository.setDeviceToken(request)
    }
}