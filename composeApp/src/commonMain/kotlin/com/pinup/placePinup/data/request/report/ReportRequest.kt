package com.pinup.placePinup.data.request.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportUserRequest(
    val reportedUserId: Int = -1,
    val reason: String,
)

@Serializable
data class ReportPinlogRequest(
    val pintsId: Int = -1,
    val reason: String,
)

@Serializable
data class ReportCommentRequest(
    val commentId: Int = -1,
    val reason: String,
)

