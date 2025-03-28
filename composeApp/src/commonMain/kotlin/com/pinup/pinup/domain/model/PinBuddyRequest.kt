package com.pinup.pinup.domain.model

data class PinBuddyRequest(
    val friendRequestStatus: String,
    val id: Int,
    val receiver: Profile,
    val sender: Profile
)
