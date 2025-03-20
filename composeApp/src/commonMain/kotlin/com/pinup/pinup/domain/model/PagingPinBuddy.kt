package com.pinup.pinup.domain.model

data class PagingPinBuddy(
    val profiles: List<Profile>,
    val totalElements: Int,
    val totalPages: Int
)
