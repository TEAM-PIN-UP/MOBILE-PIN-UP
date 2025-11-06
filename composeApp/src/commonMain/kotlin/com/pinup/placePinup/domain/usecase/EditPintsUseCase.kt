package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.pints.ModifyPintsRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.PintsRepository

class EditPintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(pintsId: Int, request: ModifyPintsRequest): PResult<Int> {
        return pintsRepository.editPints(pintsId, request)
    }
}