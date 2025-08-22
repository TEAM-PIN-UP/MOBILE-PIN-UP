package com.pinup.pinup.data.response

import com.pinup.pinup.data.response.AuthorInfoResponse.Companion.toModel
import com.pinup.pinup.data.response.CommentResponse.Companion.toModel
import com.pinup.pinup.data.response.ReplyCommentResponse.Companion.toModel
import com.pinup.pinup.domain.model.AuthorInfo
import com.pinup.pinup.domain.model.Comment
import com.pinup.pinup.domain.model.PinlogDetail
import com.pinup.pinup.domain.model.ReplyComment
import kotlinx.serialization.Serializable

@Serializable
data class GetPinlogDetailResponse(
    val id: Int = 0,
    val placeName: String = "",
    val content: String = "",
    val createdAt: String = "",
    val visitedDate: String = "",
    val starRating: Double = 0.0,
    val authorReviewCount: Int = 0,
    val writerProfileImageUrl: String = "",
    val reviewImageUrls: List<String> = emptyList(),
    val isOwn: Boolean = false,
    val likeCount: Int = 0,
    val isLikedByUser: Boolean = false,
    val commentCount: Int = 0,
    val comments: List<CommentResponse> = emptyList(),
    val isScrapByUser: Boolean = false,
) {
    companion object {
        fun GetPinlogDetailResponse.toModel(): PinlogDetail {
            return PinlogDetail(
                id = id,
                placeName = placeName,
                content = content,
                createdAt = createdAt,
                visitedDate = visitedDate,
                starRating = starRating,
                authorReviewCount = authorReviewCount,
                writerProfileImageUrl = writerProfileImageUrl,
                reviewImageUrls = reviewImageUrls,
                isOwn = isOwn,
                likeCount = likeCount,
                isLikedByUser = isLikedByUser,
                commentCount = commentCount,
                comments = comments.map {
                    it.toModel()
                },
                isScrapByUser = isScrapByUser,
            )
        }
    }
}

@Serializable
data class CommentResponse(
    val id: Int = 0,
    val content: String = "",
    val parentId: Int = 0,
    val isOwn: Boolean = false,
    val author: AuthorInfoResponse = AuthorInfoResponse(),
    val replies: List<ReplyCommentResponse> = emptyList(),
    val createdAt: String = ""
) {
    companion object {
        fun CommentResponse.toModel(): Comment {
            return Comment(
                id = id,
                content = content,
                parentId = parentId,
                isOwn = isOwn,
                author = author.toModel(),
                replies = replies.map {
                    it.toModel()
                },
                createdAt = createdAt,
            )
        }
    }
}

@Serializable
data class ReplyCommentResponse(
    val id: Int = 0,
    val content: String = "",
    val parentId: Int = 0,
    val isOwn: Boolean = false,
    val author: AuthorInfoResponse = AuthorInfoResponse(),
    val createdAt: String = "",
){
    companion object {
        fun ReplyCommentResponse.toModel(): ReplyComment {
            return ReplyComment(
                id = id,
                content = content,
                parentId = parentId,
                isOwn = isOwn,
                author = author.toModel(),
                createdAt = createdAt,
            )
        }
    }
}

@Serializable
data class AuthorInfoResponse(
    val id: Int = 0,
    val profileImageUrls: String = "",
    val nickname: String = ""
) {
    companion object {
        fun AuthorInfoResponse.toModel(): AuthorInfo {
            return AuthorInfo(
                id = id,
                profileImageUrls = profileImageUrls,
                nickname = nickname,
            )
        }
    }
}
