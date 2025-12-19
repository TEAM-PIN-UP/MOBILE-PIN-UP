package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.request.EmailVerifyRequest
import com.pinup.placePinup.data.request.SendVerifyCodeRequest
import com.pinup.placePinup.data.request.report.ReportCommentRequest
import com.pinup.placePinup.data.request.report.ReportPinlogRequest
import com.pinup.placePinup.data.request.report.ReportUserRequest
import com.pinup.placePinup.data.request.report.UserBlockRequest
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface ReportApi {
    @POST(ApiPath.Report.BLOCK_USER)
    suspend fun blockUser(@Body request: UserBlockRequest): PResult<PResponse<Unit>>
    @POST(ApiPath.Report.REPORT_USER)
    suspend fun reportUser(@Body request: ReportUserRequest): PResult<PResponse<Unit>>
    @POST(ApiPath.Report.REPORT_PINLOG)
    suspend fun reportPinlog(@Body request: ReportPinlogRequest): PResult<PResponse<Unit>>
    @POST(ApiPath.Report.REPORT_COMMENT)
    suspend fun reportComment(@Body request: ReportCommentRequest): PResult<PResponse<Unit>>
    @DELETE(ApiPath.Report.UNDO_BLOCK_USER)
    suspend fun unBlockUser(@Path("blockedUserId") request: Int): PResult<PResponse<Unit>>
}