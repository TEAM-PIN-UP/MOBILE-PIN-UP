package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.EditorPintsDetail
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.repository.PintsRepository

class GetEditorPintsDetailUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(pintsId: Int): PResult<EditorPintsDetail> {
        return pintsRepository.getEditorPintsDetail(pintsId)
    }
}