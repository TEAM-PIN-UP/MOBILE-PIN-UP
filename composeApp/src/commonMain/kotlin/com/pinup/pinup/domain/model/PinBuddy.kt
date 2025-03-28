package com.pinup.pinup.domain.model

data class PinBuddy(
    val profile: Profile,
    val relationType: RelationType,
    val friendRequestId: Int?
)