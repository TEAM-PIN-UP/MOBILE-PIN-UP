package com.pinup.pinup.domain.model

data class Member(
    val profile: Profile,
    val relationType: RelationType,
    val friendRequestId: Int?
)