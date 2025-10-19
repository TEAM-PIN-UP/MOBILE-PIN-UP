package com.pinup.pinup.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PinchListItem(
    val id: Int = -1,
    val title: String = "",
    val createdAt: String = "",
    val description: String = "",
    val author: String = "",
    val imageUrl: String? = "",
    val keywordList: List<String> = emptyList()
)
