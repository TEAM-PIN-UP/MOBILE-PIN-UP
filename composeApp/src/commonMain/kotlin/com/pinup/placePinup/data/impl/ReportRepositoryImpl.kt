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
        TODO("Not yet implemented")
    }

    override suspend fun reportPinlog(request: ReportPinlogRequest): PResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun reportUser(request: ReportUserRequest): PResult<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun reportComment(request: ReportCommentRequest): PResult<Unit> {
        TODO("Not yet implemented")
    }
}