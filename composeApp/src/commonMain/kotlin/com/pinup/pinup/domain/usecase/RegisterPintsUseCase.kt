package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.pints.ModifyPintsRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.PintsRepository

class RegisterPintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(request: ModifyPintsRequest): PResult<Int> {
        return pintsRepository.registerPints(request)
    }
}