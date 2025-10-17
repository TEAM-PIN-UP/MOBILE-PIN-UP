package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.pints.ModifyPintsRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.PintsRepository

class EditPintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(pintsId: Int, request: ModifyPintsRequest): PResult<Unit> {
        return pintsRepository.editPints(pintsId, request)
    }
}