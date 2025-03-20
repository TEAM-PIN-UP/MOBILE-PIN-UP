package com.pinup.pinup.remote.impl

import com.pinup.pinup.data.remote.ReviewsRemoteDataSource
import com.pinup.pinup.data.request.PlaceRequest
import com.pinup.pinup.data.request.ReviewRequest
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.remote.api.ReviewsApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ReviewsRemoteDataSourceImpl (
    private val reviewsApi: ReviewsApi,
) : ReviewsRemoteDataSource {
    override suspend fun registerReviews(
        files: List<File>,
        reviewRequest: ReviewRequest,
        placeRequest: PlaceRequest
    ): PResult<PResponse<String>> {
        val reviewRequestBody = Json.encodeToString(reviewRequest).toRequestBody("application/json".toMediaTypeOrNull())
        val placeRequestBody = Json.encodeToString(placeRequest).toRequestBody("application/json".toMediaTypeOrNull())
        val multipartBody = files.map {
            MultipartBody.Part.createFormData("multipartFiles", it.name, it.asRequestBody("image/jpeg".toMediaTypeOrNull()))
        }
        return reviewsApi.registerReviews(
            files = multipartBody,
            reviewRequest = reviewRequestBody,
            placeRequest = placeRequestBody
        )
    }
}