package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.report.UserBlockRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReportRepository

class PostUserBlockUseCase (
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(request: UserBlockRequest): PResult<Unit> {
        return reportRepository.blockUser(request)
    }
}