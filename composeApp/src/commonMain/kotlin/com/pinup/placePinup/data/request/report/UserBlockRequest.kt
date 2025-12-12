package com.pinup.placePinup.data.request.report

import kotlinx.serialization.Serializable

@Serializable
data class UserBlockRequest(
    val blockedUserId: Int = -1
)
