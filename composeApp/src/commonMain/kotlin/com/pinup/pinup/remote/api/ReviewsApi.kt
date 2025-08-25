package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.request.review.CommentEditRequest
import com.pinup.pinup.data.request.review.CommentRequest
import com.pinup.pinup.data.response.GetPinlogDetailResponse
import com.pinup.pinup.data.response.GetReviewsResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
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
    }

    @POST(ApiPath.PinLog.PINLOG)
    suspend fun registerReviews(
        @Body request: AddReviewRequest
    ): PResult<PResponse<String>>

    @GET(ApiPath.PinLog.PINLOG)
    suspend fun getReviews(
        @Query(QUERY_CURSOR_ID) cursor: Int,
        @Query(QUERY_SIZE) size: Int,
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