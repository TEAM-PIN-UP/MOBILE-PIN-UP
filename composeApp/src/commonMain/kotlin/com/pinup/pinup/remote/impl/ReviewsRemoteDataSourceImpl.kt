package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.data.request.PlaceRequest
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.remote.api.ReviewsApi
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class ReviewsRemoteDataSourceImpl (
    private val reviewsApi: ReviewsApi,
) : ReviewsRemoteDataSource {
    override suspend fun registerReviews(
        files: List<ByteArray>,
        reviewRequest: ReviewRequest,
        placeRequest: PlaceRequest
    ): PResult<PResponse<String>> {
        val timeStamp = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).nanosecond
        val reviewRequestString = Json.encodeToString(reviewRequest)
        val placeRequestString = Json.encodeToString(placeRequest)
        val multipart = MultiPartFormDataContent(formData {
            append("reviewRequest", reviewRequestString)
            append("placeRequest", placeRequestString)
            files.forEach {
                append("multipartFiles", it, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=$timeStamp.png")
                })
            }
        })
        return reviewsApi.registerReviews(
            multipart = multipart
        )
    }
}