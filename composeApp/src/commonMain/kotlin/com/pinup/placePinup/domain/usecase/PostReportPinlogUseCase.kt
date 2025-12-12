package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.report.ReportPinlogRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReportRepository

class PostReportPinlogUseCase (
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(request: ReportPinlogRequest): PResult<Unit> {
        return reportRepository.reportPinlog(request)
    }
}