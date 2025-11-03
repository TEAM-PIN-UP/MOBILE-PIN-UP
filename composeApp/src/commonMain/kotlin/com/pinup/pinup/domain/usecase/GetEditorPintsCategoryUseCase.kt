package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PintsCategory
import com.pinup.pinup.domain.repository.PintsRepository

class GetEditorPintsCategoryUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(): PResult<List<PintsCategory>> {
        return pintsRepository.getEditorPintsCategory()
    }
}