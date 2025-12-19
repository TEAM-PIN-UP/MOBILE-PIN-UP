package com.pinup.placePinup.data.remote

import com.pinup.placePinup.data.request.report.ReportCommentRequest
import com.pinup.placePinup.data.request.report.ReportPinlogRequest
import com.pinup.placePinup.data.request.report.ReportUserRequest
import com.pinup.placePinup.data.request.report.UserBlockRequest
import com.pinup.placePinup.domain.model.PResult

interface ReportRemoteDataSource {
    suspend fun blockUser(request: UserBlockRequest): PResult<Unit>
    suspend fun reportPinlog(request: ReportPinlogRequest): PResult<Unit>
    suspend fun reportUser(request: ReportUserRequest): PResult<Unit>
    suspend fun reportComment(request: ReportCommentRequest): PResult<Unit>
    suspend fun unBlockUser(request: Int): PResult<Unit>
}