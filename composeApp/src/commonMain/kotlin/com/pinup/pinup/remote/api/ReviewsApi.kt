package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.pinlog.AddReviewRequest
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST

interface ReviewsApi {

    @Multipart
    @POST(ApiPath.PinLog.PINLOG)
    suspend fun registerReviews(
        @Body request: AddReviewRequest
    ): PResult<PResponse<String>>
}