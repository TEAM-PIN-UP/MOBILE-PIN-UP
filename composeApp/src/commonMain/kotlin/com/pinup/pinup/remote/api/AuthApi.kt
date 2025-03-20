package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import com.pinup.pinup.domain.model.TokenInfo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {
    @POST("api/auth/refresh")
    suspend fun refreshToken(@Header("Refresh") token: String): PResult<PResponse<LoginResponse>>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): PResult<PResponse<LoginResponse>>

    @POST("api/auth/login")
    suspend fun logout(@Header("Access") token: String): PResult<PResponse<Unit>>

    @Multipart
    @POST("api/auth/sign-up")
    suspend fun signUp(
        @Part file: MultipartBody.Part,
        @Part("signUpRequest") request: RequestBody
    ): PResult<PResponse<Unit>>
}