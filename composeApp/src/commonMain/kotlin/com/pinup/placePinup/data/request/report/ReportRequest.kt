package com.pinup.placePinup.data.request.report

data class ReportUserRequest(
    val reportedUserId: Int = -1,
    val reason: String,
)

data class ReportPinlogRequest(
    val pintsId: Int = -1,
    val reason: String,
)
data class ReportCommentRequest(
    val commentId: Int = -1,
    val reason: String,
)

