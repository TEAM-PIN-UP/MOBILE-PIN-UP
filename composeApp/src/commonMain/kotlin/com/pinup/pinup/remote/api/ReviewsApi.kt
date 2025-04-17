package com.pinup.pinup.remote.api

import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.request.forms.MultiPartFormDataContent

interface ReviewsApi {

    @Multipart
    @POST("api/reviews")
    suspend fun registerReviews(
        @Body multipart: MultiPartFormDataContent
    ): PResult<PResponse<String>>
}