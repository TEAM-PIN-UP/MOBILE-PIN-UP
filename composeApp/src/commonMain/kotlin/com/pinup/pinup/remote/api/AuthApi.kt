package com.pinup.pinup.remote.api

import com.pinup.pinup.data.request.EmailLoginRequest
import com.pinup.pinup.data.request.LoginRequest
import com.pinup.pinup.data.response.LoginResponse
import com.pinup.pinup.data.response.PResponse
import com.pinup.pinup.domain.model.PResult
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Multipart
import de.jensklingenberg.ktorfit.http.POST
import io.ktor.client.request.forms.MultiPartFormDataContent

interface AuthApi {
    @POST(AuthPath.REFRESH)
    suspend fun refreshToken(@Header("Refresh") token: String): PResult<PResponse<LoginResponse>>

    @POST(AuthPath.SOCIAL_LOGIN)
    suspend fun socialLogin(@Body request: LoginRequest): PResult<PResponse<LoginResponse>>

    @POST(AuthPath.SOCIAL_LOGIN)
    suspend fun emailLogin(@Body request: EmailLoginRequest): PResult<PResponse<LoginResponse>>

    @POST(AuthPath.LOGOUT)
    suspend fun logout(@Header("Access") token: String): PResult<PResponse<Unit>>

    //TODO memeber API로 이전
    @Multipart
    @POST("api/auth/sign-up")
    suspend fun signUp(
        @Body multipart: MultiPartFormDataContent
    ): PResult<PResponse<Unit>>
}