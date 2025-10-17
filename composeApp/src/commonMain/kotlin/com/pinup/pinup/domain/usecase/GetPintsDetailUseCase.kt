package com.pinup.pinup.domain.usecase

import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PintsDetail
import com.pinup.pinup.domain.repository.PintsRepository

class GetPintsDetailUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(pintsId: Int): PResult<PintsDetail> {
        return pintsRepository.getPintsDetail(pintsId)
    }
}