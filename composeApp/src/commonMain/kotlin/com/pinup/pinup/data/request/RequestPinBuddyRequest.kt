package com.pinup.pinup.data.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestPinBuddyRequest(
    val receiverId: Int,
)
