package com.pinup.placePinup.remote.impl

import com.pinup.placePinup.data.remote.ReportRemoteDataSource
import com.pinup.placePinup.data.request.report.ReportCommentRequest
import com.pinup.placePinup.data.request.report.ReportPinlogRequest
import com.pinup.placePinup.data.request.report.ReportUserRequest
import com.pinup.placePinup.data.request.report.UserBlockRequest
import com.pinup.placePinup.domain.model.PResult
import com.pinup.placePinup.domain.model.mapSuccessData
import com.pinup.placePinup.remote.api.ReportApi

class ReportRemoteDataSourceImpl(
    private val reportApi: ReportApi
): ReportRemoteDataSource{
    override suspend fun blockUser(request: UserBlockRequest): PResult<Unit> {
        return reportApi.blockUser(request).mapSuccessData()
    }

    override suspend fun reportPinlog(request: ReportPinlogRequest): PResult<Unit> {
        return reportApi.reportPinlog(request).mapSuccessData()
    }

    override suspend fun reportUser(request: ReportUserRequest): PResult<Unit> {
        return reportApi.reportUser(request).mapSuccessData()
    }

    override suspend fun reportComment(request: ReportCommentRequest): PResult<Unit> {
        return reportApi.reportComment(request).mapSuccessData()
    }
}