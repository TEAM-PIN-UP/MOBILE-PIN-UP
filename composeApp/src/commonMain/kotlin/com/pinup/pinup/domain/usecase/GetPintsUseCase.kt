package com.pinup.pinup.domain.usecase

import com.pinup.pinup.data.request.pints.PageAble
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.PintsPageAble
import com.pinup.pinup.domain.repository.PintsRepository

class GetPintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(memberId: Int, pageAble: PageAble): PResult<PintsPageAble> {
        return pintsRepository.getPints(memberId, pageAble)
    }
}