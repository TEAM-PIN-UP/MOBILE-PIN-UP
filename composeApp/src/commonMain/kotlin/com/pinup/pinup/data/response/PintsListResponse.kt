package com.pinup.pinup.data.response

import kotlinx.serialization.Serializable

@Serializable
data class PintsListResponse(
    val content: List<PintsItemResponse> = emptyList(),
    val last: Boolean = true,
    val totalPages: Int = -1,
    val totalElements: Int = -1,
)

@Serializable
data class PintsItemResponse(
    val pintsId: Int = -1,
    val title: String = "",
    val content: String = "",
)