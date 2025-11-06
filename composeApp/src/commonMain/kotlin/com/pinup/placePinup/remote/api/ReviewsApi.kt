package com.pinup.placePinup.remote.api

import com.pinup.placePinup.data.request.ReviewRequest
import com.pinup.placePinup.data.request.pinlog.AddReviewRequest
import com.pinup.placePinup.data.request.review.CommentEditRequest
import com.pinup.placePinup.data.request.review.CommentRequest
import com.pinup.placePinup.data.response.GetPinlogDetailResponse
import com.pinup.placePinup.data.response.GetReviewsResponse
import com.pinup.placePinup.data.response.PResponse
import com.pinup.placePinup.data.response.RegisterReviewResponse
import com.pinup.placePinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface ReviewsApi {

    companion object {
        const val PATH_REVIEW_ID = "reviewId"
        const val PATH_COMMENT_ID = "commentId"
        const val QUERY_CURSOR_ID = "cursor"
        const val QUERY_SIZE = "size"
        const val QUERY_MEMBER_ID = "searchMemberId"
        const val QUERY_KEYWORD = "keyword"
    }

    @POST(ApiPath.PinLog.PINLOG)
    suspend fun registerReviews(
        @Body request: AddReviewRequest
    ): PResult<PResponse<RegisterReviewResponse>>

    @GET(ApiPath.PinLog.PINLOG)
    suspend fun getReviews(
        @Query(QUERY_CURSOR_ID) cursor: Int? = null,
        @Query(QUERY_SIZE) size: Int,
        @Query(QUERY_MEMBER_ID) searchMemberId: Int? = null,
        @Query(QUERY_KEYWORD) keyword: String? = null,
    ): PResult<PResponse<GetReviewsResponse>>

    @DELETE(ApiPath.PinLog.LIKE)
    suspend fun deleteReviewLike(
        @Path(PATH_REVIEW_ID) reviewId: Int
    ): PResult<PResponse<Unit>>

    @POST(ApiPath.PinLog.LIKE)
    suspend fun addReviewLike(
        @Path(PATH_REVIEW_ID) reviewId: Int
    ): PResult<PResponse<Unit>>

    @GET(ApiPath.PinLog.PINLOG_MODIFY)
    suspend fun getReviewDetail(
        @Path(PATH_REVIEW_ID) reviewId: Int
    ): PResult<PResponse<GetPinlogDetailResponse>>

    @DELETE(ApiPath.PinLog.PINLOG_MODIFY)
    suspend fun deleteReview(
        @Path(PATH_REVIEW_ID) reviewId: Int
    ): PResult<PResponse<Unit>>

    @PUT(ApiPath.PinLog.PINLOG_MODIFY)
    suspend fun editReview(
        @Path(PATH_REVIEW_ID) reviewId: Int,
        @Body request : ReviewRequest,
    ): PResult<PResponse<Unit>>

    @POST(ApiPath.PinLog.COMMENT)
    suspend fun addComment(
        @Path(PATH_REVIEW_ID) reviewId: Int,
        @Body request: CommentRequest
    ): PResult<PResponse<Unit>>

    @DELETE(ApiPath.PinLog.COMMENT_MODIFY)
    suspend fun deleteReview(
        @Path(PATH_REVIEW_ID) reviewId: Int,
        @Path(PATH_COMMENT_ID) commentId: Int
    ): PResult<PResponse<Unit>>

    @PUT(ApiPath.PinLog.COMMENT_MODIFY)
    suspend fun editReview(
        @Path(PATH_REVIEW_ID) reviewId: Int,
        @Path(PATH_COMMENT_ID) commentId: Int,
        @Body request : CommentEditRequest,
    ): PResult<PResponse<Unit>>
}