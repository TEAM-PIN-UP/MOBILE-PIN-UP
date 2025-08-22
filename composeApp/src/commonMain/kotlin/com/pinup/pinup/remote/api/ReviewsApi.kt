package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.response.GetPinlogDetailResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface ReviewsApi {

    @POST(ApiPath.PinLog.PINLOG)
    suspend fun registerReviews(
        @Body request: AddReviewRequest
    ): PResult<PResponse<String>>

    @GET(ApiPath.PinLog.PINLOG_MODIFY)
    suspend fun getReviewDetail(
        @Path("reviewId") reviewId: Int
    ): PResult<PResponse<GetPinlogDetailResponse>>

    @DELETE(ApiPath.PinLog.PINLOG_MODIFY)
    suspend fun deleteReview(
        @Path("reviewId") reviewId: Int
    ): PResult<PResponse<Unit>>

    @PUT(ApiPath.PinLog.PINLOG_MODIFY)
    suspend fun editReview(
        @Path("reviewId") reviewId: Int,
        @Body request : ReviewRequest,
    ): PResult<PResponse<Unit>>
}