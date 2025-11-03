package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.GetReviewedPlacesRequest
import com.pinup.pinup.data.request.pints.GetEditorPintsRequest
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PinchListItem
import com.pinup.pinup.domain.repository.PintsRepository

class GetEditorPintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(request: GetEditorPintsRequest): PResult<List<PinchListItem>> {
        return pintsRepository.getEditorPints(request)
    }
}