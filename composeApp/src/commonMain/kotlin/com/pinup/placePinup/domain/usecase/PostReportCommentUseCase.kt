package com.pinup.placePinup.domain.usecase

import com.pinup.placePinup.data.request.report.ReportCommentRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReportRepository

class PostReportCommentUseCase (
    private val reportRepository: ReportRepository,
) {
    suspend operator fun invoke(request: ReportCommentRequest): PResult<Unit> {
        return reportRepository.reportComment(request)
    }
}