package com.pinup.pinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PinchListItem(
    val id: Int,
    val title: String,
    val description: String,
)
