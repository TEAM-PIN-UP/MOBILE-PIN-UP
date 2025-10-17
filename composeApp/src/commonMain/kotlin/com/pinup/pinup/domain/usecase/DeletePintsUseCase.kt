package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.PintsRepository

class DeletePintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(pintsId: Int): PResult<Unit> {
        return pintsRepository.deletePints(pintsId)
    }
}