package com.pinup.placePinup.domain.model

data class PagingPinBuddyRequest(
    val pinBuddyRequests: List<PinBuddyRequest> = emptyList(),
    val totalElements: Int = 0,
    val totalPages: Int = 0
)
