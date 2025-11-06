package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.pints.ModifyPintsRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.PintsRepository

class RegisterPintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(request: ModifyPintsRequest): PResult<Int> {
        return pintsRepository.registerPints(request)
    }
}