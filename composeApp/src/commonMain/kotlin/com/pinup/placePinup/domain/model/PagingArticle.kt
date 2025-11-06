package com.pinup.placePinup.domain.model

data class PagingArticle(
    val articles: List<ArticlePlace> = emptyList(),
    val hasNext: Boolean = true,
    val nextCursor: Int? = null,
)