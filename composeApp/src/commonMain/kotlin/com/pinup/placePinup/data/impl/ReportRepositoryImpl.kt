package com.pinup.placePinup.data.impl

import com.pinup.placePinup.data.remote.ReportRemoteDataSource
import com.pinup.placePinup.data.request.report.ReportCommentRequest
import com.pinup.placePinup.data.request.report.ReportPinlogRequest
import com.pinup.placePinup.data.request.report.ReportUserRequest
import com.pinup.placePinup.data.request.report.UserBlockRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.repository.ReportRepository

class ReportRepositoryImpl (
    private val reportRemoteDataSource: ReportRemoteDataSource,
) : ReportRepository {
    override suspend fun blockUser(request: UserBlockRequest): PResult<Unit> {
        return reportRemoteDataSource.blockUser(request)
    }

    override suspend fun reportPinlog(request: ReportPinlogRequest): PResult<Unit> {
        return reportRemoteDataSource.reportPinlog(request)
    }

    override suspend fun reportUser(request: ReportUserRequest): PResult<Unit> {
        return reportRemoteDataSource.reportUser(request)
    }

    override suspend fun reportComment(request: ReportCommentRequest): PResult<Unit> {
        return reportRemoteDataSource.reportComment(request)
    }

    override suspend fun undoBlock(request: Int): PResult<Unit> {
        return reportRemoteDataSource.unBlockUser(request)
    }
}