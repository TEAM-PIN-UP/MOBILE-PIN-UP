package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.EditorPintsDetail
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.PintsRepository

class GetEditorPintsDetailUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(pintsId: Int): PResult<EditorPintsDetail> {
        return pintsRepository.getEditorPintsDetail(pintsId)
    }
}