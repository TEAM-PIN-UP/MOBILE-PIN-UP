package com.pinup.pinup.remote.api

import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ReviewsApi(
    private val httpClient: HttpClient
) {

    suspend fun registerReviews(
        files: List<ByteArray>,
        reviewRequest: String,
        placeRequest: String,
    ): PResult<PResponse<String>> {
        val response = httpClient.post("api/reviews") {
            contentType(ContentType.MultiPart.FormData)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        for (file in files) {
                            append("multipartFiles", file)
                        }
                        append("reviewRequest", reviewRequest)
                        append("placeRequest", placeRequest)
                    }
                )
            )
        }
        return response.body()
    }
}