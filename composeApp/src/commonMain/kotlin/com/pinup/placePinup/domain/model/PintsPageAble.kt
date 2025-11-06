package com.pinup.placePinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PintsPageAble(
    val content: List<PintsItem> = emptyList(),
    val last: Boolean = true,
    val totalPages: Int = -1,
    val totalElements: Int = -1,
)

@Serializable
data class PintsItem(
    val pintsId: Int = -1,
    val title: String = "",
    val content: String = "",
)
