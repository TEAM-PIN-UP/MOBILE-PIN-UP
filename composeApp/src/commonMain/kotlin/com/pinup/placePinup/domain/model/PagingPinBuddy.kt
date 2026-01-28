package com.pinup.placePinup.domain.model

data class PagingPinBuddy(
    val profiles: List<Profile> = emptyList(),
    val totalElements: Int = 0,
    val totalPages: Int = 0
)
