package com.pinup.pinup.remote.api

import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReviewsApi {

    @Multipart
    @POST("api/reviews")
    suspend fun registerReviews(
        @Part files: List<MultipartBody.Part>,
        @Part("reviewRequest") reviewRequest: RequestBody,
        @Part("placeRequest") placeRequest: RequestBody,
    ): PResult<PResponse<String>>
}