package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReportRepository

class PostUserUnBlockUseCase (
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(request: Int): PResult<Unit> {
        return reportRepository.undoBlock(request)
    }
}