package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.pints.PageAble
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.PintsPageAble
import com.pinup.placePinup.domain.repository.PintsRepository

class GetPintsUseCase (
    private val pintsRepository: PintsRepository,
) {
    suspend operator fun invoke(memberId: Int, pageAble: PageAble): PResult<PintsPageAble> {
        return pintsRepository.getPints(memberId, pageAble)
    }
}