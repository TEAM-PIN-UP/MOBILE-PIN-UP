package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.pints.GetEditorPintsRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PinchListItem
import com.pinup.placePinup.domain.repository.PintsRepository

class GetEditorPintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(request: GetEditorPintsRequest): PResult<List<PinchListItem>> {
        return pintsRepository.getEditorPints(request)
    }
}