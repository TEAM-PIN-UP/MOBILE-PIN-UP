package com.pinup.pinup.domain.model

data class PagingPinBuddyRequest(
    val pinBuddyRequests: List<PinBuddyRequest>,
    val totalElements: Int,
    val totalPages: Int
)
