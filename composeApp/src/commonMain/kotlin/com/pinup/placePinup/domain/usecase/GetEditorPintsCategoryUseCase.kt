package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PintsCategory
import com.pinup.placePinup.domain.repository.PintsRepository

class GetEditorPintsCategoryUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(): PResult<List<PintsCategory>> {
        return pintsRepository.getEditorPintsCategory()
    }
}