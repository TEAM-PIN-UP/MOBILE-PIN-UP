package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.report.ReportUserRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReportRepository

class PostReportUserUseCase (
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(request: ReportUserRequest): PResult<Unit> {
        return reportRepository.reportUser(request)
    }
}