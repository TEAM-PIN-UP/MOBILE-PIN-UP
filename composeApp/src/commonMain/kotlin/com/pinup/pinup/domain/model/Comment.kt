package com.pinup.pinup.domain.model


data class Comment(
    val id: Int = 0,
    val content: String = "",
    val parentId: Int = 0,
    val isOwn: Boolean = false,
    val author: AuthorInfo = AuthorInfo(),
    val replies: List<ReplyComment> = emptyList(),
    val createdAt: String = ""
)

data class ReplyComment(
    val id: Int = 0,
    val content: String = "",
    val parentId: Int = 0,
    val isOwn: Boolean = false,
    val author: AuthorInfo = AuthorInfo(),
    val createdAt: String = "",
)

data class AuthorInfo(
    val id: Int = 0,
    val profileImageUrl: String = "",
    val nickname: String = ""
)
