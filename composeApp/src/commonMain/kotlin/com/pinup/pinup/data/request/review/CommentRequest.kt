package com.pinup.pinup.data.request.review

import kotlinx.serialization.Serializable

@Serializable
data class CommentRequest(
    val content: String,
    val parentId: Int?
)

@Serializable
data class CommentEditRequest(
    val content: String,
)
