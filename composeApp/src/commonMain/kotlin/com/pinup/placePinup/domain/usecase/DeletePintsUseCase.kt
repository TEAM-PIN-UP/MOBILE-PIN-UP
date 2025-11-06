package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.PintsRepository

class DeletePintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(pintsId: Int): PResult<Unit> {
        return pintsRepository.deletePints(pintsId)
    }
}