package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PintsDetail
import com.pinup.placePinup.domain.repository.PintsRepository

class GetPintsDetailUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(pintsId: Int): PResult<PintsDetail> {
        return pintsRepository.getPintsDetail(pintsId)
    }
}