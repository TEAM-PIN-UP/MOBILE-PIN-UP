package com.pinup.pinup.domain.model

data class  ArticleDetail(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val writer: String = "",
    val createdAt: String = "",
    val image: List<String> = emptyList(),
    val content: String = "",
    val place: List<Place> = emptyList(),
)